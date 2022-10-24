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

package cn.chenyunlong.common.model;

import lombok.Data;

import java.util.List;

@Data
public class PageResult<T> {

    public PageResult() {
    }

    private Long total;
    private Integer totalPages;
    private Integer pageSize;
    private Integer pageNumber;
    private List<T> list;

    public PageResult(List<T> list, Long total, Integer pageSize, Integer pageNumber) {
        this.list = list;
        this.total = total;
        this.pageSize = pageSize;
        this.pageNumber = pageNumber;
    }

    public static <T> PageResult of(List<T> list, Long total, Integer pageSize, Integer pageNumber) {
        return new PageResult(list, total, pageSize, pageNumber);
    }
}
