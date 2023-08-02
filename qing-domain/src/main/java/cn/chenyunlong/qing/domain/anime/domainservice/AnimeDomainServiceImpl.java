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

package cn.chenyunlong.qing.domain.anime.domainservice;

import cn.chenyunlong.qing.domain.anime.AnimeInfo;
import cn.chenyunlong.qing.domain.anime.creator.AnimeInfoCreator;
import cn.chenyunlong.qing.domain.anime.domainservice.model.biz.BatchRecommendModel;
import cn.chenyunlong.qing.domain.anime.domainservice.model.biz.TransferModel;
import cn.chenyunlong.qing.domain.anime.domainservice.model.meta.InOutBizType;
import cn.chenyunlong.qing.domain.anime.repository.AnimeInfoRepository;
import cn.chenyunlong.qing.domain.anime.service.IAnimeInfoService;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnimeDomainServiceImpl implements IAnimeDomainService {

    private final IAnimeInfoService animeInfoService;
    private final AnimeInfoRepository animeInfoRepository;


    @Override
    @Transactional
    public void handleAnimeInfoRecommend(BatchRecommendModel batchRecommendModel) {
        Assert.notEmpty(batchRecommendModel.getAnimeIds());
        String genBatchNo = IdUtil.simpleUUID();
        AnimeInfoBizInfo animeInfoBizInfo = AnimeInfoBizInfo
            .builder()
            .batchNo(batchRecommendModel.getBatchNo())
            .genBatchNo(genBatchNo)
            .inOutBizType(batchRecommendModel.getInOutBizType())
            .uniqueCodes(batchRecommendModel.getAnimeIds())
            .operateUser(batchRecommendModel.getOperateUser())
            .build();

        //查询所有的动漫信息
        List<AnimeInfo> animeInfoList =
            animeInfoRepository.findAllById(batchRecommendModel.getAnimeIds());
    }

    @Override
    @Transactional
    public void handleAnimeInfoTransfer(TransferModel transferModel) {
        Assert.notEmpty(transferModel.getUniqueCodes());
        String genBatchNo = IdUtil.simpleUUID();
        BatchRecommendModel outModel = new BatchRecommendModel();
        outModel.setBatchNo(transferModel.getBatchNo());
        outModel.setInOutBizType(InOutBizType.OUT_TRANSFER);
        outModel.setOperateUser(transferModel.getOperateUser());
        outModel.setHouseId(transferModel.getTransferOutHouseId());
        outModel.setAnimeIds(transferModel.getUniqueCodes());
        handleAnimeInfoOut(outModel);
        log.info("处理出库完成，仓库id:{},批次号:{},自动批号:{}",
            transferModel.getTransferOutHouseId(), transferModel.getBatchNo(), genBatchNo);

//        Optional<AnimeInfo> animeInfo = animeInfoRepository.findById(transferModel.getUniqueCodes().get(0));
//        BatchInOutModel inModel = new BatchInOutModel();
//        inModel.setAnimeIds(transferModel.getUniqueCodes());
//        inModel.setName(animeInfo.get().getName());
//        inModel.setInOutBizType(InOutBizType.IN_TRANSFER);
//        inModel.setSkuId(transferModel.getSkuId());
//        inModel.setOperateUser(transferModel.getOperateUser());
//        inModel.setBatchNo(transferModel.getBatchNo());
//        handleAnimeInfoRecommend(inModel);
//        log.info("处理入库完成，仓库id:{},批次号:{},自动批号:{}",
//                transferModel.getTransferOutHouseId(),
//                transferModel.getBatchNo(),
//                genBatchNo);
    }

    @Override
    @Transactional
    public void handleAnimeInfoOut(BatchRecommendModel batchRecommendModel) {
        Assert.notEmpty(batchRecommendModel.getAnimeIds());
        String genBatchNo = IdUtil.simpleUUID();
        AnimeInfoBizInfo bizInfo = AnimeInfoBizInfo
            .builder()
            .batchNo(batchRecommendModel.getBatchNo())
            .genBatchNo(genBatchNo)
            .inOutBizType(batchRecommendModel.getInOutBizType())
            .uniqueCodes(batchRecommendModel.getAnimeIds())
            .operateUser(batchRecommendModel.getOperateUser())
            .build();
//        batchInOutModel.getUniqueCodes()
//                .stream()
//                .forEach(code -> {
//                    BooleanBuilder bb = new BooleanBuilder()
//                            .and(QAsset.asset.houseId.eq(batchInOutModel.getHouseId()))
//                            .and(QAsset.asset.uniqueCode.eq(code))
//                            .and(QAsset.asset.skuId.eq(batchInOutModel.getSkuId()));
//                    Optional<Asset> old = animeInfoRepository.findOne(bb);
//                    if (!old.isPresent()) {
//                        throw new BusinessException(AssetErrorCode.ASSET_CODE_NOT_EXIST, "资产编码:" + code);
//                    } else {
//                        EntityOperations
//                                .doUpdate(animeInfoRepository)
//                                .load(() -> old.get())
//                                .update(asset -> asset.out(bizInfo))
//                                .execute();
//                    }
//                });
    }

    /**
     * 添加动漫信息
     *
     * @param animeInfoCreator 转移模型
     * @return 创建动漫信息
     */
    @Override
    public Long create(AnimeInfoCreator animeInfoCreator) {
        return animeInfoService.createAnimeInfo(animeInfoCreator);
    }
}
