/*
 * Copyright (c) 2019-2023  YunLong Chen
 * Project Qing is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 *
 */

package cn.chenyunlong.common.enums;

import cn.chenyunlong.common.constants.BaseEnum;

/**
 * <p>
 * 前后端返回码。
 * </p>
 *
 * @author 陈云龙
 * @since 2022/11/27
 */
public enum ResponseCode implements BaseEnum<Integer> {
    // 成功
    SUCCESS(1000, "成功"),

    // 1___流程通用问题
    // 参数错误
    PARAM_FAIL(1001, "参数错误"), // 用户无操作权限 No_authority
    NO_AUTHORITY(1004, "无操作权限"),

    // 21___充值问题
    // 不支持的充值渠道
    CHARGE_CHANNEL_NO_SUPPORT(2101, "不支持该渠道"), // 充值金额过低
    CHARGE_VALUE_TOO_LOW(2102, "充值金额过低"), // 充值金额过高
    CHARGE_VALUE_TOO_HIGH(2103, "充值金额过高"), // 无该充值订单
    CHARGE_RECORD_MISS(2104, "无该充值订单"),

    // 支付宝订单异常
    CHARGE_ALIPAY_FORM_EXCEPTION(2121, "生成ALIPAY表单异常"), // 微信订单异常
    CHARGE_WECHAT_FORM_EXCEPTION(2122, "生成WECHAT表单异常"),

    // 充值记录更新失败
    CHARGE_RECORD_UPDATE_FAIL(2201, "充值记录早已被更改"), // 充值记录更新失败
    CHARGE_ACCOUNT_ADD_FAIL(2202, "账户余额增加失败"),

    // nat cross系列
    //
    CREATE_NEW_LISTEN_FAIL(3001, "创建监听服务失败"), //
    UPDATE_LISTEN_FAIL(3002, "更新创建监听服务失败"), //
    LISTEN_PORT_HAS(3003, "端口已存在"), //
    SAVE_NEW_LISTEN_FAIL(3004, "保存监听服务信息"), //
    LISTEN_PORT_NO_HAS(3005, "端口不存在"),

    // 6、签名问题
    //
    SIGN_OVER_TIME(6001, "请求超过最大差异"), //
    SIGN_ERROR(6002, "签名错误"), //
    SIGN_FILE_ERROR(6003, "验签时文件异常"),

    // 8、检查系别
    // 密码不符合设定
    PASSWORD_ILLEGAL(8001, "密码不合法"), // 注册用户名过长
    NAME_ILLEGAL(8002, "用户名非法"), // 手机号非法
    MOBILE_ILLEGAL(8003, "手机号非法"),

    // 95、注册系别
    // 注册时异常
    REGIS_EXCEPTION(9500, "信息异常"), // 用户已存在
    REGIS_USER_HAS(9501, "用户已存在"),

    // 96、登录系别
    // 登录异常
    LOGIN_EXCEPTION(9600, "登录时发生异常"), // 不是理想的登录名，及不知道怎么搜索
    LOGIN_NAME_INVALID(9601, "登录名有误"), // 不存在该用户
    LOGIN_USER_MISS(9602, "不存在该用户"), // 密码错误
    LOGIN_PASSWORD_INVALID(9603, "密码错误"), // 其他地点登录
    LOGIN_OTHER_PLACE(9604, "在其他地点进行了登录"),

    // 97__请求时用户问题
    // 用户不存在
    USER_NO_HAS(9700, "用户不存在"), // 在其他地点登录，或刷新过token
    USER_RENEWAL(9701, "TOKEN已变更"),

    // 98__token异常
    // 有的信息不存在，建议更新token
    TOKEN_MISTAKE(9800, "token有误"), // token过期
    TOKEN_OVERDUE(9801, "token过期"), // 校验失败、不符合对象等
    TOKEN_INVALID(9802, "token无效"), // 未知错误
    TOKEN_EXCEPTION(9899, "token解析异常"),

    // 数据操作异常
    DATA_OPR_FAIL(9998, "数据操作异常"), // 未知错误
    FAIL(9999, "未知错误");

    private final Integer code;
    private final String name;

    ResponseCode(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public Integer getValue() {
        return code;
    }

    @Override
    public String getName() {
        return name;
    }
}
