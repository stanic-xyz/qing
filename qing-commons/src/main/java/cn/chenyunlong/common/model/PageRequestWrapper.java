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

import cn.hutool.core.collection.CollUtil;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Nonnegative;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

/**
 * 分页包装器
 *
 * @author 陈云龙
 */
@Data
@JsonFilter("getWrapper")
public class PageRequestWrapper<T> {

    /**
     * 查询条件
     */
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
    private Map<String, String> sorts;

    /**
     * 获取分页信息。
     *
     * @return 分页请求
     */
    @Schema(hidden = true)
    @JsonIgnore
    @JsonIgnoreProperties
    public PageRequest getWrapper() {
        Sort sort = Sort.unsorted();
        if (CollUtil.isNotEmpty(sorts)) {
            sort = Sort.by(sorts.entrySet().stream().map(entry -> {
                String entryValue = entry.getValue();
                Sort.Direction direction = Sort.Direction.fromOptionalString(entryValue).orElse(Sort.Direction.DESC);
                return Sort.Order.by(entry.getKey()).with(direction);
            }).collect(Collectors.toList()));
        }
        return PageRequest.of(page, pageSize).withSort(sort);
    }
}
