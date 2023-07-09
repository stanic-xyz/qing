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

package cn.chenyunlong.qing.domain.anime.domainservice;


import cn.chenyunlong.qing.domain.anime.creator.AnimeInfoCreator;
import cn.chenyunlong.qing.domain.anime.domainservice.model.biz.BatchRecommendModel;
import cn.chenyunlong.qing.domain.anime.domainservice.model.biz.TransferModel;

public interface IAnimeDomainService {

    /**
     * 处理动漫信息推荐
     * 资产入库
     *
     * @param batchRecommendModel 批量输入输出模型
     */
    void handleAnimeInfoRecommend(BatchRecommendModel batchRecommendModel);


    /**
     * 处理动画信息输出
     * 资产出库
     *
     * @param batchRecommendModel 批量输入输出模型
     */
    void handleAnimeInfoOut(BatchRecommendModel batchRecommendModel);

    /**
     * 处理动画信息传输
     * 资产调拨，转移
     *
     * @param transferModel 转移模型
     */
    void handleAnimeInfoTransfer(TransferModel transferModel);

    /**
     * 添加动漫信息
     *
     * @param animeInfoCreator 动漫发布信息
     */
    Long create(AnimeInfoCreator animeInfoCreator);
}
