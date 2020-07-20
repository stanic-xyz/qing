package com.stan.zhangli.handler.command;

/**
 * @author:郭境科
 * @createdate:2020年4月10日
 * @description:请求指令
 */
public interface ReqCommand {

	Integer LOGIN_REQUEST_CODE = 1000; //初始连接验证身份
	
	Integer GETALLUNREADMSGCOUNT_REQUEST_CODE = 1001; //获取用户的未读消息总数
	
	Integer HEARTBEAT_REQUEST_CODE = 1002;//心跳检测请求包指令
	
	Integer SINGLECHATMSG_REQUEST_CODE = 1100;//单聊消息指令包
	
	Integer GROUPCHATMSG_REQUEST_CODE = 1101;//群聊消息指令包
	
	Integer REVOKEMSG_REQUEST_CODE = 1102;//撤回消息
	
	Integer ACKOFARRIVE_REQUEST_CODE = 1103;//ack消息送达包
	
	Integer ACKOFREAD_REQUEST_CODE = 1104;//ack消息已读包
	
	Integer REVOKEMSG_ACK_REQUEST_CODE = 1105;//撤回消息ack
	
	Integer QUERYSESSIONS_REQUEST_CODE = 1200;//获取会话记录列表
	
	Integer QUERYONESESSION_REQUEST_CODE = 1201;//获取单个会话记录
	
	Integer TOPSESSION_REQUEST_CODE = 1202;//置顶/取消置顶会话记录
	
	Integer DELETESESSION_REQUEST_CODE = 1203;//删除会话
	
	Integer SHIELDSESSION_REQUEST_CODE = 1204;//屏蔽会话
	
	Integer NODISTURBSESSION_REQUEST_CODE = 1205;//消息免打扰
	
	Integer RECENT_HISTORYRECORD_INITIAL_REQUEST_CODE = 1300;//查询近期历史记录(初始加载近期历史记录)
	
	Integer RECENT_HISTORYRECORD_DIRECT_REQUEST_CODE = 1301;//查询近期历史记录(直接查询所有未读消息)
	
	Integer RECENT_HISTORYRECORD_PAGE_REQUEST_CODE = 1302;//查询近期历史记录(分页查询消息)
	
	Integer RECENT_ALLHISTORYRECORD_REQUEST_CODE = 1303;//查询全部历史记录
	
	Integer CLEAN_HISTORYRECORD_REQUEST_CODE = 1304;//清空历史记录
	
	Integer RECENT_TEACHER_HISTORYRECORD_REQUEST_CODE = 1305;//查询老师的历史消息
}
