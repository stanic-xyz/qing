package com.stan.zhangli.channel.attribute;

import io.netty.util.AttributeKey;

import java.util.concurrent.ConcurrentHashMap;

public interface Attributes {
    AttributeKey<String> UserID = AttributeKey.valueOf("UserID");
    AttributeKey<Integer> ClientType = AttributeKey.valueOf("ClientType");
    AttributeKey<String> Authorization = AttributeKey.valueOf("Authorization");
    AttributeKey<ConcurrentHashMap<Long, Object>> MSG_ACKED_MAP = AttributeKey.valueOf("msg_acked_map");
    AttributeKey<ConcurrentHashMap<Long, Object>> REV_ACKED_MAP = AttributeKey.valueOf("rev_acked_map");
}
