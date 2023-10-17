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

import java.util.List;
import lombok.Data;

@Data
public class PageResult<T> {

    private Long total;
    private Integer totalPages;
    private Integer pageSize;
    private Integer pageNumber;
    private List<T> list;

    public PageResult() {
    }

    /**
     * 构造分页信息。
     *
     * @param list       实体类
     * @param total      总共数量
     * @param pageSize   分页大小
     * @param pageNumber 页码
     */
    public PageResult(List<T> list, Long total, Integer pageSize, Integer pageNumber) {
        this.list = list;
        this.total = total;
        this.pageSize = pageSize;
        this.pageNumber = pageNumber;
    }

    /**
     * 根据数据类型构建分页对象。
     *
     * @param list       数据列表
     * @param total      总数量
     * @param pageSize   分页大小
     * @param pageNumber 页码
     * @param clazz      数据类型信息
     */
    public PageResult(List<T> list, Long total, Integer pageSize, Integer pageNumber,
                      Class<T> clazz) {
        this.list = list;
        this.total = total;
        this.pageSize = pageSize;
        this.pageNumber = pageNumber;
    }

    public static <T> PageResult<T> of(List<T> list, Long total, Integer pageSize,
                                       Integer pageNumber) {
        return new PageResult<>(list, total, pageSize, pageNumber);
    }

    public static <T> PageResult<T> of(List<T> list, Long total, Integer pageSize,
                                       Integer pageNumber, Class<T> clazz) {
        return new PageResult<>(list, total, pageSize, pageNumber, clazz);
    }
}
