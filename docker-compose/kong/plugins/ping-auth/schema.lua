local network_handler = require("kong.plugins.ping-auth.network_handler")

local function url_validator(url)
    local parsed_url = network_handler.parse_url(string.lower(url))
    if parsed_url.scheme ~= "http" and parsed_url.scheme ~= "https" then
        return false, "URL scheme must be either 'http' or 'https'"
    elseif parsed_url.host == nil or parsed_url.host == "" then
        return false, "URL host cannot be blank"
    end
    return true
end

return {
    name = "ping-auth",
    fields = {
        { config = {
            type = "record",
            fields = {
                { service_url = { type = "string", required = true, custom_validator = url_validator }, },
                { shared_secret = { type = "string", required = true }, },
                { secret_header_name = { type = "string", required = true }, },
                { connection_timeout_ms = { type = "integer", required = false, default = 10000, gt = 0 }, },
                { connection_keepAlive_ms = { type = "integer", required = false, default = 60000, gt = 0 }, },
                { verify_service_certificate = { type = "boolean", required = false, default = true }, },
                { enable_debug_logging = { type = "boolean", required = false, default = false }, },
            },
        }, },
    },
    entity_checks = {}
}
