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

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.Map;
import javax.annotation.Nonnegative;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

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
    @Min(1)
    @NotNull
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

    /**
     * 获取分页信息
     *
     * @return
     */
    public PageRequest getWrapper() {
        Sort sort = Sort.unsorted();
        return PageRequest.of(page, pageSize, sort);
    }
}
