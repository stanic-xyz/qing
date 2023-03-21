package cn.chenyunlong.qing.domain.anime.anime.domainservice;


import cn.chenyunlong.qing.domain.anime.anime.domainservice.model.biz.BatchInOutModel;
import cn.chenyunlong.qing.domain.anime.anime.domainservice.model.biz.TransferModel;

public interface IAnimeDomainService {

    /**
     * 资产入库
     */
    void handleAnimeInfoRecommend(BatchInOutModel batchInOutModel);


    /**
     * 资产出库
     */
    void handleAnimeInfoOut(BatchInOutModel batchInOutModel);

    /**
     * 资产调拨，转移
     */
    void handleAnimeInfoTransfer(TransferModel transferModel);
}
