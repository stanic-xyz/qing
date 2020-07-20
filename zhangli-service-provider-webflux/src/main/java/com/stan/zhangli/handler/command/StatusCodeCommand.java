package com.stan.zhangli.handler.command;

/**
 * @author:郭境科
 * @createdate:2020年4月10日
 * @description:状态码
 */
public interface StatusCodeCommand {
	
	Integer SUCCESS_CODE = 200; //请求响应成功
	Integer WRONGFUL_PARAMETER_CODE = 400; //请求参数不合法
	Integer UNAUTHORIZED_REQUEST_CODE = 401; //未认证的token
	Integer NO_ACCESS_CODE = 403; //无权访问
	Integer SERVER_EXCEPTION_CODE = 500; //服务器异常
	
	String SUCCESS_MESSAGE = "success"; //请求响应成功
	String WRONGFUL_PARAMETER_MESSAGE = "请求参数不合法"; //请求参数不合法
	String UNAUTHORIZED_REQUEST_MESSAGE = "未认证的连接"; //未认证的token
	String NO_ACCESS_MESSAGE = "无权访问"; //无权访问
	String SERVER_EXCEPTION_MESSAGE = "服务器异常"; //服务器异常

}
