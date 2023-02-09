package cn.chenyunlong.qing.domain.anime.anime.domainservice;


import cn.chenyunlong.qing.domain.anime.anime.domainservice.model.BatchInOutModel;
import cn.chenyunlong.qing.domain.anime.anime.domainservice.model.TransferModel;

public interface IAnimeDomainService {

    /**
     * 资产入库
     */
    void handleAnimeInfoIn(BatchInOutModel batchInOutModel);


    /**
     * 资产出库
     */
    void handleAnimeInfoOut(BatchInOutModel batchInOutModel);

    /**
     * 资产调拨，转移
     */
    void handleAnimeInfoTransfer(TransferModel transferModel);
}
