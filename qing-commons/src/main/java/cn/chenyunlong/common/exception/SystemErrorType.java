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

package cn.chenyunlong.common.exception;


/**
 * 自定义错误类型
 *
 * @author 陈云龙
 */

public enum SystemErrorType implements ErrorType {
    /**
     * 具体的错误类型以及消息提示信息
     */
    ARGUMENT_NOT_VALID("020000", "请求参数校验不通过"),
    DUPLICATE_PRIMARY_KEY("030000", "唯一键冲突"),
    FAILED("500", "操作失败"),
    GATEWAY_CONNECT_TIME_OUT("010002", "网关超时"), GATEWAY_ERROR("010500", "网关异常"),
    GATEWAY_NOT_FOUND_SERVICE("010404", "服务未找到"), INVALID_TOKEN("020001", "无效token"),
    SUCCESS("200", "操作成功"), SYSTEM_BUSY("000001", "系统繁忙,请稍候再试"),
    SYSTEM_ERROR("-1", "系统异常"), UNAUTHORIZED("401", "暂未登录或token已经过期"),
    FORBIDDEN("403", "没有相关权限"), UPLOAD_FILE_SIZE_LIMIT("020010", "上传文件大小超过限制"),
    VALIDATE_FAILED("404", "参数检验失败");

    /**
     * 错误类型码
     */
    private final String code;
    /**
     * 错误类型描述信息
     */
    private final String msg;

    SystemErrorType(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }
}
