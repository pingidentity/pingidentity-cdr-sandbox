local BasePlugin = require("kong.plugins.base_plugin")
local access = require("kong.plugins.ping-auth.access")
local response = require("kong.plugins.ping-auth.response")

local kong_response = kong.response

local NAME = "[ping-auth] "

-- Extend Kong's base plugin to provide basic DEBUG level logging of the plugin lifecycles
local PingHandler = BasePlugin:extend()

PingHandler.VERSION = "1.0.3-SNAPSHOT"
PingHandler.PRIORITY = 999

-- These objects are used to pass request information between the "access" and "response" phases
PingHandler.request = {}
PingHandler.state = {}

function PingHandler:new()
    PingHandler.super.new(self, "ping-auth")
end

--[[
    Tie into Kong's access phase to make the first sideband call. Basic error handling is done to ensure this phase
    rejects any requests if an unexpected error is encountered (fail-closed)
        config = the Kong provided plugin configuration
        return: nil
]]
function PingHandler:access(config)
    PingHandler.super.access(self)
    local ok
    ok, PingHandler.request, PingHandler.state = pcall(access.execute, config)

    if not ok then
        ngx.log(ngx.ERR, string.format("%sEncountered unexpected error: %s", NAME, PingHandler.request))
        return kong_response.exit(500)
    end
end

--[[
    Tie into Kong's response phase to make the second sideband call. Basic error handling is done to ensure this phase
    rejects any requests if an unexpected error is encountered (fail-closed)
        config = the Kong provided plugin configuration
        return: nil
]]
function PingHandler:response(config)
    PingHandler.super.response(self)
    local ok, err = pcall(response.execute, config, PingHandler.request, PingHandler.state)

    if not ok then
        ngx.log(ngx.ERR, string.format("%sEncountered unexpected error: %s", NAME, err))
        return kong_response.exit(500)
    end
end

return PingHandler
