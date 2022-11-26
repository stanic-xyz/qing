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

package cn.chenyunlong.qing.domain.sign.service;

import cn.chenyunlong.qing.domain.sign.Sign;
import cn.chenyunlong.qing.domain.system.mapper.SignMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Stan
 */
@Service
public class SignServiceImpl implements SignService {

    @Autowired
    private SignMapper signMapper;


    @Override
    public int getSignStatus(Integer userId) {
        return signMapper.getSignState(userId);
    }

    @Override
    public void addSign(Sign sign) {
        signMapper.add(sign);
    }
}
