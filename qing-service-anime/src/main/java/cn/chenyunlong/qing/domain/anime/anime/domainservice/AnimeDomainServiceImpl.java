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

package cn.chenyunlong.qing.domain.anime.anime.domainservice;

import cn.chenyunlong.jpa.support.EntityOperations;
import cn.chenyunlong.qing.domain.anime.anime.AnimeInfo;
import cn.chenyunlong.qing.domain.anime.anime.creator.AnimeInfoCreator;
import cn.chenyunlong.qing.domain.anime.anime.domainservice.model.biz.BatchInOutModel;
import cn.chenyunlong.qing.domain.anime.anime.domainservice.model.biz.TransferModel;
import cn.chenyunlong.qing.domain.anime.anime.domainservice.model.meta.InOutBizType;
import cn.chenyunlong.qing.domain.anime.anime.mapper.AnimeInfoMapper;
import cn.chenyunlong.qing.domain.anime.anime.repository.AnimeInfoRepository;
import cn.chenyunlong.qing.domain.anime.recommend.repository.RecommendRepository;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnimeDomainServiceImpl implements IAnimeDomainService {

    private final AnimeInfoRepository animeInfoRepository;
    private final RecommendRepository recommendRepository;

    @Override
    @Transactional
    public void handleAnimeInfoRecommend(BatchInOutModel batchInOutModel) {
        Assert.notEmpty(batchInOutModel.getAnimeIds());
        String genBatchNo = IdUtil.simpleUUID();
        AnimeInfoBizInfo animeInfoBizInfo = AnimeInfoBizInfo
                .builder()
                .batchNo(batchInOutModel.getBatchNo())
                .genBatchNo(genBatchNo)
                .inOutBizType(batchInOutModel.getInOutBizType())
                .uniqueCodes(batchInOutModel.getAnimeIds())
                .operateUser(batchInOutModel.getOperateUser())
                .build();

        //查询所有的动漫信息
        animeInfoRepository.findAllById(batchInOutModel.getAnimeIds()).forEach(animeInfo -> {
            // 判断是否存在
            Optional<AnimeInfo> animeInfoOptional = animeInfoRepository.findById(animeInfo.getId());
            animeInfoOptional.ifPresent(info -> {
                //如果已经存在则更新入库
                EntityOperations
                        .doUpdate(animeInfoRepository)
                        .load(animeInfoOptional::get)
                        .update(asset -> asset.in(animeInfoBizInfo))
                        .execute();
            });
            if (animeInfoOptional.isEmpty()) {
                //不存在则直接入库
                AnimeInfoCreator creator = new AnimeInfoCreator();
//                        creator.setName(batchInOutModel.getHouseId());
                creator.setName(batchInOutModel.getName());
//                        creator.setSkuId(batchInOutModel.getSkuId());
//                        creator.setUniqueCode(animeInfo);

                AnimeInfo dtoToEntity = AnimeInfoMapper.INSTANCE.dtoToEntity(creator);

                EntityOperations
                        .doCreate(animeInfoRepository)
                        .create(() -> dtoToEntity)
                        .update(anime -> anime.in(animeInfoBizInfo))
                        .execute();
            }
        });
    }

    @Override
    @Transactional
    public void handleAnimeInfoOut(BatchInOutModel batchInOutModel) {
        Assert.notEmpty(batchInOutModel.getAnimeIds());
        String genBatchNo = IdUtil.simpleUUID();
        AnimeInfoBizInfo bizInfo = AnimeInfoBizInfo
                .builder()
                .batchNo(batchInOutModel.getBatchNo())
                .genBatchNo(genBatchNo)
                .inOutBizType(batchInOutModel.getInOutBizType())
                .uniqueCodes(batchInOutModel.getAnimeIds())
                .operateUser(batchInOutModel.getOperateUser())
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

    @Override
    @Transactional
    public void handleAnimeInfoTransfer(TransferModel transferModel) {
        Assert.notEmpty(transferModel.getUniqueCodes());
        String genBatchNo = IdUtil.simpleUUID();
        BatchInOutModel outModel = new BatchInOutModel();
        outModel.setBatchNo(transferModel.getBatchNo());
        outModel.setInOutBizType(InOutBizType.OUT_TRANSFER);
        outModel.setOperateUser(transferModel.getOperateUser());
        outModel.setHouseId(transferModel.getTransferOutHouseId());
        outModel.setAnimeIds(transferModel.getUniqueCodes());
        handleAnimeInfoOut(outModel);
        log.info("处理出库完成，仓库id:{},批次号:{},自动批号:{}", transferModel.getTransferOutHouseId(), transferModel.getBatchNo(),
                genBatchNo);

        Optional<AnimeInfo> animeInfo = animeInfoRepository.findById(transferModel.getUniqueCodes().get(0));
        BatchInOutModel inModel = new BatchInOutModel();
        inModel.setAnimeIds(transferModel.getUniqueCodes());
        inModel.setName(animeInfo.get().getName());
        inModel.setInOutBizType(InOutBizType.IN_TRANSFER);
        inModel.setSkuId(transferModel.getSkuId());
        inModel.setOperateUser(transferModel.getOperateUser());
        inModel.setBatchNo(transferModel.getBatchNo());
        handleAnimeInfoRecommend(inModel);
        log.info("处理入库完成，仓库id:{},批次号:{},自动批号:{}", transferModel.getTransferOutHouseId(), transferModel.getBatchNo(),
                genBatchNo);
    }
}