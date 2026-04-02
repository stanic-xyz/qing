-- KEYS[1] : 商品库存的 Redis key
-- ARGV[1] : 扣减数量（默认为1）
local stock = redis.call('get', KEYS[1])
if not stock then
    return 0   -- 库存未初始化
end
if tonumber(stock) >= tonumber(ARGV[1]) then
    redis.call('decrby', KEYS[1], ARGV[1])
    return 1
end
return 0
