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

package cn.chenyunlong.common.constants;

/**
 * 错误代码枚举。
 *
 * @author gim
 */

public enum CodeEnum implements BaseEnum<Integer> {
    /**
     * 整个系统通用编码 xx_xx_xxxx。 (服务标识_业务_错误编号，便于错误快速定位）
     */
    Success(1, "操作成功"),
    Fail(0, "操作失败"),

    CreateError(10010, "创建失败"),
    NotFoundError(10001, "未查询到信息"),
    SaveError(10002, "保存信息失败"),
    UpdateError(10003, "更新信息失败"),
    ValidateError(10004, "数据检验失败"),
    StatusHasValid(10005, "状态已经被启用"),
    StatusHasInvalid(10006, "状态已经被禁用"),
    SystemError(10007, "系统异常"),
    BusinessError(10008, "业务异常"),
    ParamSetIllegal(10009, "参数设置非法"),
    TransferStatusError(10010, "当前状态不正确，请勿重复提交"),
    NotGrant(10011, "没有操作该功能的权限，请联系管理员"),


    USERNAME_EXIST(10012, "用户名已存在"),
    ROLE_NAME_EXIST(10013, "角色名已存在");
    private final Integer code;
    private final String msg;

    CodeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String getName() {
        return this.msg;
    }

    /**
     * 获取枚举的值。
     *
     * @return 获取枚举的值
     */
    @Override
    public Integer getValue() {
        return code;
    }
}
