/**
 * @author:郭境科
 * @createdate:2020年4月17日
 * @description:reids的key名称
 */
package com.stan.zhangli.handler.command;

public interface RedisKeyName {
	
	/**
	 * 用户总未读数
	 */
	//String USER_UNREAD_COUNT = "puin:cha:uc:";
	
	/**
	 * 用户的单聊会话未读数
	 */
	String USER_SESSION_S_UNREAD_COUNT = "puin:cha:sucs:";
	
	/**
	 * 用户的群聊会话未读数
	 */
	String USER_SESSION_G_UNREAD_COUNT = "puin:cha:sucg:";
	
	/**
	 * 会话消息
	 */
	String SESSION_MSEEAGE = "puin:cha:smg:";
	
	/**
	 * 事务锁key
	 */
	String SESSION_MSEEAGE_TRANSACTION_KEY = "puin:cha:smg:tan";
	
	/**
	 *群组成员
	 */
	String GROUPMEMBERS_BYID = "puin:cha:gms:";
	
	/**
	 *群组成员模糊查询
	 */
	String GROUPMEMBERS_BYID_INDISTINCT = "puin:cha:gms:*";
}
