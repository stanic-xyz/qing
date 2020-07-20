package com.stan.zhangli.handler.command;

/**
 * @author:郭境科
 * @createdate:2020年4月12日
 * @description:返回码
 */
public interface BackCodeCommand {

	Integer MEANINGLESS_CODE = 0;//返回码为0，表示无意义
	
	Integer EXCEPTION_ONE_CODE = 1;//业务情况1
	
	Integer EXCEPTION_TWO_CODE = 2;//业务情况2
}
