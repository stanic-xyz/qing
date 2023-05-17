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

package cn.chenyunlong.common.model;

import lombok.Data;

import javax.annotation.Nonnegative;
import javax.annotation.Nullable;
import java.util.Map;

/**
 * @author gim
 */
@Data
public class PageRequestWrapper<T> {

    /**
     * 查询条件
     */
    @Nullable
    private T bean;

    /**
     * 分页大小
     */
    @Nonnegative
    private Integer pageSize = 12;

    /**
     * 当前页
     */
    @Nonnegative
    private Integer page = 1;


    /**
     * 排序信息
     */
    @Nullable
    private Map<String, String> sorts;
}
