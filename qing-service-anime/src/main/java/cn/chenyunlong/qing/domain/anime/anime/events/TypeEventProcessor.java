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

package cn.chenyunlong.qing.domain.anime.anime.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class TypeEventProcessor {

//    private final IAssetLifecycleService assetLifecycleService;
//
//    private final IWarehouseService warehouseService;
//
//    private final IAssetInOutRecordService assetInOutRecordService;

    @EventListener
    public void handleAssetInForLifecycle(AnimeInfoEvents.AnimeInfoInEvent inEvent) {
//        AssetBizInfo bizInfo = inEvent.bizInfo();
//        Asset asset = inEvent.animeInfo();
//        /**
//         * 艺术性的处理采用批号的方式进行批量插入操作，避免每次事件都插入一条记录，这样有性能问题
//         */
//        List<AssetLifecycleCreator> assetLifecycleCreators = bizInfo.getUniqueCodes().stream()
//                .map(code -> {
//                    AssetLifecycleCreator creator = new AssetLifecycleCreator();
//                    creator.setAssetsId(asset.getId());
//                    creator.setHouseId(asset.getHouseId());
//                    creator.setName(asset.getName());
//                    //不用关注findById 那是仓库服务应该考虑的事，最少知道原则，那个优化可以加缓存
//                    creator.setHouseName(warehouseService.findById(asset.getHouseId()).getName());
//                    creator.setInOutBizType(bizInfo.getInOutBizType());
//                    creator.setUniqueCode(code);
//                    creator.setRemark("入库");
//                    creator.setBatchNo(bizInfo.getBatchNo());
//                    creator.setGenBatchNo(bizInfo.getGenBatchNo());
//                    creator.setInOutType(InOutType.IN);
//                    creator.setSkuId(asset.getSkuId());
//                    creator.setOperateUser(bizInfo.getOperateUser());
//                    return creator;
//                }).collect(Collectors.toList());
//        assetLifecycleService.batchCreateLifecycle(bizInfo.getGenBatchNo(), assetLifecycleCreators);
    }

    @EventListener
    public void handleAssetOutForLifecycle(AnimeInfoEvents.AnimeInfoOutEvent outEvent) {
//        AnimeInfoBizInfo bizInfo = outEvent.bizInfo();
//        AnimeInfo asset = outEvent.animeInfo();
//        /**
//         * 艺术性的处理采用批号的方式进行批量插入操作，避免每次事件都插入一条记录，这样有性能问题
//         */
//        List<AssetLifecycleCreator> assetLifecycleCreators = bizInfo.getUniqueCodes().stream()
//                .map(code -> {
//                    AssetLifecycleCreator creator = new AssetLifecycleCreator();
//                    creator.setAssetsId(asset.getId());
//                    creator.setHouseId(asset.getHouseId());
//                    creator.setName(asset.getName());
//                    //不用关注findById 那是仓库服务应该考虑的事，最少知道原则，那个优化可以加缓存
//                    creator.setHouseName(warehouseService.findById(asset.getHouseId()).getName());
//                    creator.setInOutBizType(bizInfo.getInOutBizType());
//                    creator.setUniqueCode(code);
//                    creator.setRemark("出库");
//                    creator.setBatchNo(bizInfo.getBatchNo());
//                    creator.setSkuId(asset.getSkuId());
//                    creator.setGenBatchNo(bizInfo.getGenBatchNo());
//                    creator.setInOutType(InOutType.OUT);
//                    creator.setOperateUser(bizInfo.getOperateUser());
//                    return creator;
//                }).collect(Collectors.toList());
//        assetLifecycleService.batchCreateLifecycle(bizInfo.getGenBatchNo(), assetLifecycleCreators);
    }

    /**
     * 保存出库记录
     *
     * @param outEvent
     */
    @EventListener
    public void handleAssetOutForRecord(AnimeInfoEvents.AnimeInfoOutEvent outEvent) {
//        AssetBizInfo bizInfo = outEvent.getBizInfo();
//        AssetInOutRecordCreator creator = new AssetInOutRecordCreator();
//        creator.setInOutBizType(bizInfo.getInOutBizType());
//        creator.setInOutType(InOutType.OUT);
//        creator.setBatchNo(bizInfo.getBatchNo());
//        creator.setGenBatchNo(bizInfo.getGenBatchNo());
//        creator.setOperateUser(bizInfo.getOperateUser());
//        creator.setTotalCount(bizInfo.getUniqueCodes().size());
//        assetInOutRecordService.createAssetInOutRecord(bizInfo.getUniqueCodes(), creator);

    }

    /**
     * 保存入库记录
     *
     * @param inEvent
     */
    @EventListener
    public void handleAssetInForRecord(AnimeInfoEvents.AnimeInfoInEvent inEvent) {
//        AssetBizInfo bizInfo = inEvent.bizInfo();
//        AssetInOutRecordCreator creator = new AssetInOutRecordCreator();
//        creator.setInOutBizType(bizInfo.getInOutBizType());
//        creator.setInOutType(InOutType.IN);
//        creator.setBatchNo(bizInfo.getBatchNo());
//        creator.setGenBatchNo(bizInfo.getGenBatchNo());
//        creator.setOperateUser(bizInfo.getOperateUser());
//        creator.setTotalCount(bizInfo.getUniqueCodes().size());
//        assetInOutRecordService.createAssetInOutRecord(bizInfo.getUniqueCodes(), creator);

    }

}
