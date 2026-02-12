-- KEYS[1]: The rate limit key (e.g., "rate_limit:ip:127.0.0.1")
-- ARGV[1]: Current timestamp in milliseconds
-- ARGV[2]: Window size in milliseconds
-- ARGV[3]: Max allowed requests

local key = KEYS[1]
local current_time = tonumber(ARGV[1])
local window_size = tonumber(ARGV[2])
local max_requests = tonumber(ARGV[3])

-- Remove elements older than the window
local window_start = current_time - window_size
redis.call('ZREMRANGEBYSCORE', key, '-inf', window_start)

-- Count elements in the current window
local current_count = redis.call('ZCARD', key)

if current_count < max_requests then
    -- Add the current request timestamp
    redis.call('ZADD', key, current_time, current_time)
    -- Set expiry for the key to clean up if idle (window size + buffer)
    redis.call('PEXPIRE', key, window_size + 1000)
    return true
else
    return false
end
