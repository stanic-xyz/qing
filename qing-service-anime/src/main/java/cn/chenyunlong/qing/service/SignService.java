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

package cn.chenyunlong.qing.service;

import cn.chenyunlong.qing.model.entities.Sign;

/**
 * @author Stan
 */
public interface SignService {


    /**
     * 获取签到状态
     *
     * @param userId 用户ID
     * @return 返回信息
     */
    int getSignStatus(Integer userId);

    /**
     * 添加签到信息
     *
     * @param sign 签到信息
     */
    void addSign(Sign sign);
}
