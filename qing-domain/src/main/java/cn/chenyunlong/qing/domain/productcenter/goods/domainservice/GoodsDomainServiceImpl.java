package cn.chenyunlong.qing.domain.productcenter.goods.domainservice;

import cn.chenyunlong.qing.domain.productcenter.goods.domainservice.model.GoodsInStoreRequest;
import cn.chenyunlong.qing.domain.productcenter.goods.domainservice.model.GoodsOutStoreRequest;
import cn.chenyunlong.qing.domain.productcenter.goods.domainservice.model.GoodsTransferStoreRequest;
import org.springframework.stereotype.Service;

@Service
public class GoodsDomainServiceImpl implements IGoodsDomainService {

    @Override
    public void goodsIn(GoodsInStoreRequest request) {
        //判断是否已经入库
    }

    @Override
    public void goodsTransfer(GoodsTransferStoreRequest request) {

    }

    @Override
    public void goodsOut(GoodsOutStoreRequest request) {

    }

}
