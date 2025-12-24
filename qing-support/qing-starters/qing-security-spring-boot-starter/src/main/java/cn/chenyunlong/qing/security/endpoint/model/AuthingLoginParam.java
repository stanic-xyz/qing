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

package cn.chenyunlong.qing.security.endpoint.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.ToString;


/**
 * Login param.
 *
 * @author 陈云龙
 * @since 2021/04/05
 */
@Data
@ToString
public class AuthingLoginParam {

    @NotBlank(message = "code不能为空")
    private String code;

    @NotBlank(message = "state不能为空")
    private String password;
}
