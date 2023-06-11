/*
 * Copyright (c) 2023  YunLong Chen
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

package cn.chenyunlong.qing.infrastructure.converter;

import cn.chenyunlong.qing.domain.anime.anime.TaskStatus;
import jakarta.persistence.AttributeConverter;

/**
 * 对象转换器
 */
public class TaskStatusConverter implements AttributeConverter<TaskStatus, Integer> {

    /**
     * 发布信息
     * 发布信息给你看了哈发生的，我们现在就是这个样子的
     *
     * @param attribute the entity attribute value to be converted
     * @return 我发布了信息了
     */
    @Override
    public Integer convertToDatabaseColumn(TaskStatus attribute) {
        return attribute.getValue();
    }


    @Override
    public TaskStatus convertToEntityAttribute(Integer dbData) {
        return TaskStatus.of(dbData).orElse(null);
    }
}
