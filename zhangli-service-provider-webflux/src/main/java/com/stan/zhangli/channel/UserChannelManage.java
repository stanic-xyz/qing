package com.stan.zhangli.channel;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserChannelManage {

    private static final Map<String, ChannelGroup> userIdChannelGroupMap = new ConcurrentHashMap<>();

    public static void bindChannelGroup(String userId, Channel channel) {
    	ChannelGroup channelGroup = userIdChannelGroupMap.get(userId);
    	if(channelGroup!=null) {
    		channelGroup.add(channel);
    		
    	}else {
    		
    	    channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    		userIdChannelGroupMap.put(userId, channelGroup);
    	}
    	
    }

    public static ChannelGroup getChannel(String userId) {
    	ChannelGroup channelGroup = userIdChannelGroupMap.get(userId);
    	if(channelGroup==null) {
    		channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    		userIdChannelGroupMap.put(userId, channelGroup);
    	}
        return channelGroup;
    }

  
}
