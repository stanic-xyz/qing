package cn.chenyunlong.qing.domain.productcenter.goods.domainservice;


import cn.chenyunlong.qing.domain.productcenter.goods.domainservice.model.GoodsInStoreRequest;
import cn.chenyunlong.qing.domain.productcenter.goods.domainservice.model.GoodsOutStoreRequest;
import cn.chenyunlong.qing.domain.productcenter.goods.domainservice.model.GoodsTransferStoreRequest;

public interface IGoodsDomainService {

    /**
     * 商品入库
     *
     * @param request 请求
     */
    void goodsIn(GoodsInStoreRequest request);


    /**
     * 商品调拨
     *
     * @param request 请求
     */
    void goodsTransfer(GoodsTransferStoreRequest request);

    /**
     * 商品出库
     *
     * @param request 请求
     */
    void goodsOut(GoodsOutStoreRequest request);

}
