local network_handler = require("kong.plugins.ping-auth.network_handler")
local cjson = require "cjson.safe"
local x509 = require "resty.openssl.x509"

local kong_request = kong.request
local kong_response = kong.response
local kong_service_request = kong.service.request
local ngx_var = ngx.var
local ngx_req = ngx.req

local NAME = "[ping-auth] "
local SIDEBAND_REQUEST_ENDPOINT = "sideband/request"

local original_request

local _M = {}

--[[
    Send a POST request to <authorization url>/sideband/request and handle the response accordingly
        config = the Kong provided plugin configuration
        return: a table representation of the original request sent by the client, along with the state field from the
                response from the policy provider (may be nil)
]]
function _M.execute(config)
    local parsed_url = network_handler.parse_url(config.service_url)
    local request
    request, original_request = _M.compose_payload(config, parsed_url)

    if config.enable_debug_logging then
        ngx.log(ngx.DEBUG, string.format("%sSending sideband request to policy provider: \n%s",
                NAME, network_handler.request_tostring(request)))
    end

    local status_code, _, body = network_handler.execute(config, parsed_url, request)
    ngx.log(ngx.DEBUG, string.format("Status code: %s", status_code))
    local state = _M.handle_response(status_code, body)

    return original_request, state
end

--[[
    Build the sideband request
        config = the Kong provided plugin configuration
        parsed_url = the authorization URL parsed into it's component parts
        return: a table containing all the parameters needed to send an HTTP request, as well as a table
                representation of the original request sent by the client
]]
function _M.compose_payload(config, parsed_url)
    -- populate request data in the format expected by the sideband API
    local payload_body = {}
    payload_body.source_ip = ngx_var.remote_addr
    payload_body.source_port = ngx_var.remote_port
    payload_body.method = ngx_req.get_method()
    payload_body.http_version = tostring(ngx_req.http_version())
    payload_body.url = kong_request.get_forwarded_scheme() .. "://" .. kong_request.get_forwarded_host() .. ":" ..
            kong_request.get_forwarded_port() .. kong_request.get_forwarded_path() ..
            "?" .. kong_request.get_raw_query()
    payload_body.body = kong_request.get_raw_body()
    payload_body.headers = network_handler.format_headers(ngx_req.get_headers())

    local client_cert = _M.get_client_cert()
    if client_cert then
        payload_body.client_certificate = client_cert
    end

    local json_payload_body, err = cjson.encode(payload_body)
    if not json_payload_body then
        ngx.log(ngx.ERR, string.format("%sUnable to parse JSON body. Error: %s", NAME, err))
        return kong_response.exit(400)
    end

    -- parse out the full authorization path, including the sideband request endpoint
    local path = parsed_url.path
    if path:sub(-1) ~= "/" then
        path = path .. "/"
    end
    path = path .. SIDEBAND_REQUEST_ENDPOINT

    local host = parsed_url.host
    if parsed_url.port then
        host = host .. ":" .. parsed_url.port
    end

    -- combine full request
    local payload_headers = {
        ["Host"] = host,
        ["Connection"] = "Keep-Alive",
        ["Content-Type"] = "application/json",
        ["Content-Length"] = #json_payload_body,
        [config.secret_header_name] = config.shared_secret
    }

    local payload = {
        method = "POST",
        http_version = 1.1,
        path = path,
        query = parsed_url.query,
        headers = payload_headers,
        body = json_payload_body
    }

    return payload, payload_body
end

--[[
    Handle the sideband response returned from Ping
        status_code = the HTTP status code returned from the sideband API request
        body = the body of the sideband API request
        return: the state field returned by the policy provider (may be nil)
]]
function _M.handle_response(status_code, body)
    local err
    body, err = cjson.decode(body)
    if not body then
        ngx.log(ngx.ERR, string.format("%sUnable to parse JSON body returned from policy provider. Error: %s",
                NAME, err))
        return kong_response.exit(502)
    end

    -- handle failed requests
    if network_handler.is_failed_request(status_code, body) then
        return kong_response.exit(502)
    end

    -- handle denied requests
    local ping_response = body.response
    if ping_response then
        local response_code = tonumber(ping_response.response_code)
        ngx.log(ngx.DEBUG,
                string.format(
                        "Request authorization denied by the policy provider, returning status %d %s to client.",
                        response_code,
                        ping_response.response_status
                )
        )
        return kong_response.exit(response_code, ping_response.body and ping_response.body or "", network_handler.flatten_headers(ping_response.headers))
    end

    _M.update_request(body)
    return body.state
end

--[[
    Update the original request in Kong with any new/modified data from Ping
        body = the body of the sideband API request
        return: nil

    NOTE: currently the following options can't be updated in Kong, and thus modifications in the response from
          /sideband/request aren't supported: source_ip, source_port, scheme (http/s), client_certificate
]]
function _M.update_request(body)
    -- Update headers
    local original_headers = network_handler.flatten_headers(original_request.headers)
    local new_headers = network_handler.flatten_headers(body.headers)
    -- look for modified headers or headers that were removed by the policy provider
    for header, val in pairs(original_headers) do
        local found = false
        for new_header, new_val in pairs(new_headers) do
            if header == new_header then
                found = true
                if val ~= new_val then
                    ngx.log(ngx.DEBUG, string.format("%sUpdating \"%s\" header based on response from policy provider (\"%s\" => \"%s\")", NAME, header, val, new_val))
                    kong_service_request.set_header(header, new_val)
                end
                break
            end
        end
        if not found then
            ngx.log(ngx.DEBUG, string.format("%sRemoving \"%s\" header based on response from policy provider", NAME, header))
            kong_service_request.clear_header(header)
        end
    end
    -- look for headers that were added by the policy provider
    for new_header, new_val in pairs(new_headers) do
        local found = false
        for header in pairs(original_headers) do
            if header == new_header then
                found = true
                break
            end
        end
        if not found then
            ngx.log(ngx.DEBUG, string.format("%sAdding \"%s\" header based on response from policy provider (\"%s\")", NAME, new_header, new_val))
            kong_service_request.set_header(new_header, new_val)
        end
    end
    -- remove this header to avoid Gzip encoding issues
    kong_service_request.clear_header("Accept-Encoding")

    -- Update method
    if original_request.method ~= body.method then
        ngx.log(ngx.DEBUG, string.format("%sUpdating request method based on response from policy provider (\"%s\" => \"%s\")", NAME, original_request.method, body.method))
        kong_service_request.set_method(body.method)
    end

    -- Update URL parameters individually
    if original_request.url ~= body.url then
        local original_url = network_handler.parse_url(original_request.url)
        local new_url = network_handler.parse_url(body.url)

        if new_url.host and (new_url.host ~= original_url.host or new_url.port ~= original_url.port) then
            local host = new_url.host
            if new_url.port then
                host = host .. ":" .. new_url.port
            end
            ngx.log(ngx.DEBUG, string.format("%sUpdating URL host based on response from policy provider (\"%s\" => \"%s\")", NAME, original_request.url, body.url))
            kong_service_request.set_header("host", host)
        end
        if new_url.path and new_url.path ~= original_url.path then
            ngx.log(ngx.DEBUG, string.format("%sUpdating URL path based on response from policy provider (\"%s\" => \"%s\")", NAME, original_request.url, body.url))
            kong_service_request.set_path(new_url.path)
        end
        if new_url.query and new_url.query ~= original_url.query then
            ngx.log(ngx.DEBUG, string.format("%sUpdating URL query based on response from policy provider (\"%s\" => \"%s\")", NAME, original_request.url, body.url))
            kong_service_request.set_raw_query(new_url.query)
        end
        _M.log_unsupported_change(original_url.scheme, new_url.scheme)
    end

    -- check for changes that aren't supported
    _M.log_unsupported_change(original_request.source_ip, body.source_ip)
    _M.log_unsupported_change(tostring(original_request.source_port), tostring(body.source_port))
    _M.log_unsupported_change(original_request.client_certificate, body.client_certificate)
end

--[[
    Log a warning message if a change in value is detected
        original_value = the original configuration value from Kong
        new_value = the updated value returned in the response from the policy provider
        return: nil
]]
function _M.log_unsupported_change(original_value, new_value)
    if original_value ~= new_value then
        ngx.log(ngx.WARN,
                string.format(
                        "%sWarning: policy provider attempted to modify request field that's not supported. (\"%s\" => \"%s\")",
                        NAME, original_value, new_value))
    end
end

--[[
    Retrieve the client certificate provided to Kong, if present
        return: a table containing a base64 encoded DER representation of the client certificate under the "x5c" key or
                nil if the client certificate isn't present
]]
function _M.get_client_cert()
    -- retrieve client certificate
    local client_cert_pem = kong.client.tls.get_full_client_certificate_chain()
    if not client_cert_pem then
        return nil
    end

    -- parse the client certificate
    local cert, err = x509.new(kong.client.tls.get_full_client_certificate_chain(), "*")
    if err then
        ngx.log(ngx.ERR, string.format("%sFailed to parse client certificate with error: \"%s\"", NAME, err))
        return kong_response.exit(400)
    end

    -- retrieve the public key from the client certificate
    local key
    key, err = cert:get_pubkey()
    if err then
        ngx.log(ngx.ERR, string.format("%sFailed to parse public key from the client certificate with error: \"%s\"",
                NAME, err))
        return kong_response.exit(400)
    end

    -- parse the public key into a JWT
    local jwt_string = key:tostring("public", "JWK")
    local jwt
    jwt, err = cjson.decode(jwt_string)
    if err then
        ngx.log(ngx.ERR, string.format("%sUnable to decode public key from the client certificate with error: \"%s\"",
                NAME, err))
        return kong_response.exit(400)
    end

    -- append the client certificate to the JWT
    jwt.x5c = { ngx.encode_base64(cert:tostring("DER")) }

    return jwt
end

return _M
