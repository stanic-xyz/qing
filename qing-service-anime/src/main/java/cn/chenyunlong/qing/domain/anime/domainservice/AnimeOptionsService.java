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

package cn.chenyunlong.qing.domain.anime.domainservice;

import cn.chenyunlong.qing.infrastructure.model.vo.OptionsModel;

/**
 * 选项服务
 *
 * @author Stan
 */
public interface AnimeOptionsService {
    /**
     * 获取选项信息
     *
     * @return 获取所有筛选条件
     */
    OptionsModel getOptions();

    /**
     * 获取最近列表的默认分页大小
     *
     * @return 最新记录的分页大小
     */
    int getRecentPageSize();


    /**
     * 更新配置信息
     */
    void updateOptions();
}
