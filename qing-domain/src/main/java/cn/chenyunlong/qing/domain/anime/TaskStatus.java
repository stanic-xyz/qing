/*
 * Copyright (c) 2019-2023  YunLong Chen
 * Project Qing is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 *
 */

package cn.chenyunlong.qing.domain.anime;

import cn.chenyunlong.common.constants.BaseEnum;
import java.util.Optional;

public enum TaskStatus implements BaseEnum<Integer> {
    STARTED(1, "任务开始");

    private final int value;
    private final String name;

    TaskStatus(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static Optional<TaskStatus> of(Integer value) {
        return Optional.ofNullable(BaseEnum.parseByCode(value, TaskStatus.class));
    }

    /**
     * 获取code码存入数据库
     *
     * @return 获取编码
     */
    @Override
    public Integer getValue() {
        return value;
    }

    /**
     * 获取编码名称，便于维护
     *
     * @return 获取编码名称
     */
    @Override
    public String getName() {
        return name;
    }
}
