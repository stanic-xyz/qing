package com.stan.zhangli.handler.command;

/**
 * @author:郭境科
 * @createdate:2020年4月11日
 * @description:客户端类型参数指令
 */
public interface ClientTypeCommand {
    
	Integer ANDROID_CLIENT = 1;//安卓客户端设备类型参数
	
	Integer PC_CLIENT = 2;//PC客户端设备类型参数
	
	Integer IOS_CLIENT = 3;//ios客户端设备类型参数
	
	Integer WEB_MAIN_CLIENT = 41;//web客户端设备类型参数,主页面连接（在线交流主页面）
	
	Integer WEB_ASS_CLIENT = 42;//web客户端设备类型参数,非主页面连接（小助手交流信息）
	
}
