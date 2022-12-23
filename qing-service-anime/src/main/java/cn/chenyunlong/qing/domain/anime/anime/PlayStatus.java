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

package cn.chenyunlong.qing.domain.anime.anime;

import cn.chenyunlong.common.constants.BaseEnum;

public enum PlayStatus implements BaseEnum {

    FINISHED(1, "完结"),


    SERIALIZING(2, "已完成向");

    private final Integer status;
    private final String statusName;

    PlayStatus(int animeStatus, String statusName) {

        this.status = animeStatus;
        this.statusName = statusName;
    }

    /**
     * 获取code码存入数据库
     *
     * @return 获取编码
     */
    @Override
    public Integer getValue() {
        return status;
    }

    /**
     * 获取编码名称，便于维护
     *
     * @return 获取编码名称
     */
    @Override
    public String getName() {
        return statusName;
    }
}
