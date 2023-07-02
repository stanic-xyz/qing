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

package cn.chenyunlong.common.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

/**
 * User login env.
 *
 * @author Stan
 * @version 1.0
 * @date 2020/3/31 2:39 下午
 */
@Data
@ToString
@AllArgsConstructor
public class LoginPreCheckDTO {

    private Boolean needMfaCode;

}
