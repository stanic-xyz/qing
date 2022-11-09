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

package cn.chenyunlong.qing.service.impl;

import cn.chenyunlong.qing.core.exception.ServiceException;
import cn.chenyunlong.qing.domain.anime.AnimeInfo;
import cn.chenyunlong.qing.domain.feedback.AnimeFeedbackEntity;
import cn.chenyunlong.qing.mapper.AnimeFeedbackMapper;
import cn.chenyunlong.qing.mapper.AnimeInfoMapper;
import cn.chenyunlong.qing.service.ReportService;
import cn.chenyunlong.qing.utils.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class ReportServiceImpl implements ReportService {
    private final AnimeFeedbackMapper feedbackMapper;
    private final AnimeInfoMapper animeInfoMapper;

    public ReportServiceImpl(AnimeFeedbackMapper feedbackMapper, AnimeInfoMapper animeInfoMapper) {
        this.feedbackMapper = feedbackMapper;
        this.animeInfoMapper = animeInfoMapper;
    }

    @Override
    public void addReport(String username, Long cid, boolean linkInvalid, boolean someMissing, boolean badQuality, String detail) {

        AnimeInfo animeInfo = animeInfoMapper.selectById(cid);
        if (animeInfo == null) {
            throw new ServiceException("动漫信息不存在");
        }

        AnimeFeedbackEntity entity = new AnimeFeedbackEntity();

        entity.setMid(cid);
        entity.setDetail(detail);
        entity.setType(1);
        entity.setDetail(StringUtils.isNotEmpty(detail) ? detail : "资源发生了异常，请检查");
        entity.setProcessingStatus(0);
        feedbackMapper.insert(entity);
    }
}
