/*
 * Copyright (c) 2019-2022 YunLong Chen
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

package cn.chenyunlong.qing.domain.user;

import cn.chenyunlong.qing.infrastructure.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Stan
 */
@TableName(value = "user_third")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserThird extends BaseEntity<UserThird> {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 账号ID
     */
    private Long userId;

    /**
     * 用户登录的账号，长度为十位字符
     */
    private String uid;

    /**
     * 应用类型
     */
    private String appType;

    /**
     * 用户界面上展示的昵称
     */
    private String accessToken;

    /**
     * 用户的头像地址
     */
    private Integer accessExpire;
}
