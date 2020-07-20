package com.stan.zhangli.handler.command;

/**
 * @author:郭境科
 * @createdate:2020年4月10日
 * @description:推送指令
 */
public interface ResCommand {
	
	Integer EXCEPTION_RESPONSE_CODE = 400; //异常返回指令
	
	Integer LOGIN_RESPONSE_CODE = 1000; //连接验证结果
	
	Integer GETALLUNREADMSGCOUNT_RESPONSE_CODE = 1001; //获取用户的未读消息总数
	
	Integer HEARTBEAT_RESPONSE_CODE = 1002;//心跳检测响应包指令
	
	Integer CLOSELINK_RESPONSE_CODE = 500; //告知客户端关闭websocket
	String CLOSELINK_RESPONSE_MESSAGE = "关闭连接";//告知客户端关闭websocket
	
	Integer SINGLECHATMSG_RESPONSE_CODE = 1100;//单聊消息响应包
	
	Integer GROUPCHATMSG_RESPONSE_CODE = 1101;//群聊消息响应包
	String EXCEPTION_RESPONSE_0_MESSAGE = "群组类型错误或群组已被删除";//群聊异常：群组类型错误
	String EXCEPTION_RESPONSE_1_MESSAGE = "群组人数不足";//群聊异常：群组人数不足
	String EXCEPTION_RESPONSE_2_MESSAGE = "发送者用户不在该群组";//群聊异常：发送者用户不在该群组
	
	Integer REVOKEMSG_RESPONSE_CODE = 1102;//撤回消息指令，告知发送者其它客户端和所有接收端
	String REVOKEMSG_RESPONSE_0_MESSAGE = "消息不存在";//群聊异常：群组类型错误
	
	Integer ACKOFSENT_RESPONSE_CODE = 1103; //ack包“消息已发送/屏蔽”
	String ACKOFSENT_0_RESPONSE_MESSAGE = "消息已发送";//消息已发送
	String ACKOFSENT_1_RESPONSE_MESSAGE = "您已屏蔽对方，无法发送消息";//发送者已屏蔽对方，无法发送消息
	String ACKOFSENT_2_RESPONSE_MESSAGE = "您已被对方屏蔽，无法发送消息";//发送者已被接收者屏蔽，无法发送消息
	
	Integer ACKOFREAD_RESPONSE_CODE = 1104; //同步消息已读包
	
	Integer REVOKEMSG_ACK_RESPONSE_CODE = 1105;//撤回消息ack推送
	
	Integer QUERYSESSIONS_RESPONSE_CODE = 1200;//获取会话记录列表
	
	Integer QUERYONESESSION_RESPONSE_CODE = 1201;//获取单个会话记录
	
	Integer TOPSESSION_SUCC_RESPONSE_CODE = 1202;//会话置顶/取消置顶响应
	String TOPSESSION_1_RESPONSE_MESSAGE = "会话不存在";//消息已发送
	
	Integer TOPSESSION_RESPONSE_CODE = 1203;//发送“置顶会话/取消置顶”，同步会话置顶
	
	Integer DELETESESSION_SUCC_RESPONSE_CODE = 1204;//删除会话成功
	
	Integer DELETESESSION_RESPONSE_CODE = 1205;//发送“删除会话”，同步删除会话
	
	Integer SHIELDSESSION_SUCC_RESPONSE_CODE = 1206;//屏蔽会话成功
	String SHIELDSESSION_1_RESPONSE_MESSAGE = "会话不存在";//消息已发送
	
	Integer SHIELDSESSION_RESPONSE_CODE = 1207;//发送“屏蔽会话/取消屏蔽”，同步屏蔽会话/取消会话屏蔽
	
	Integer NODISTURBSESSION_SUCC_RESPONSE_CODE = 1208;//“会话免打扰/取消免打扰”设置成功
	String NODISTURBSESSION_1_RESPONSE_MESSAGE = "会话不存在";//消息已发送
	
	Integer NODISTURBSESSION_RESPONSE_CODE = 1209;//发送“会话免打扰/取消免打扰”，同步会话免打扰/取消免打扰
	
	Integer UPDATESESSIONINFO_RESPONSE_CODE = 1210;//更新会话信息
	
	Integer RECENT_HISTORYRECORD_INITIAL_RESPONSE_CODE = 1300;//发送近期历史记录(初始加载近期历史记录)
	
	Integer RECENT_HISTORYRECORD_DIRECT_RESPONSE_CODE = 1301;//发送近期历史记录(直接查询所有未读消息)
	
	Integer RECENT_HISTORYRECORD_PAGE_RESPONSE_CODE = 1302;//发送近期历史记录(分页查询消息)
	
	Integer RECENT_ALLHISTORYRECORD_RESPONSE_CODE = 1303;//发送全部历史记录数据
	
	Integer CLEAN_HISTORYRECORD_SUCC_RESPONSE_CODE = 1304;//清空历史记录成功
	
	Integer CLEAN_HISTORYRECORD_RESPONSE_CODE = 1305;//发送“清空历史记录”，同步清空
	
	Integer RECENT_TEACHER_HISTORYRECORD_RESPONSE_CODE = 1306;//发送老师的历史消息

}
