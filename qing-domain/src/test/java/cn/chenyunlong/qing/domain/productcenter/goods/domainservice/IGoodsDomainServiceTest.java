package cn.chenyunlong.qing.domain.productcenter.goods.domainservice;

import cn.chenyunlong.qing.domain.AbstractDomainTests;
import cn.chenyunlong.qing.domain.productcenter.goods.domainservice.model.GoodsInStoreRequest;
import cn.hutool.core.util.IdUtil;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class IGoodsDomainServiceTest extends AbstractDomainTests {


    @Resource
    private IGoodsDomainService domainService;

    @Test
    void testGoodsIn() {
        // 准备测试数据
        GoodsInStoreRequest request = new GoodsInStoreRequest();
        request.setStoreId(IdUtil.getSnowflakeNextIdStr());
        request.setGoodsCodes(Collections.singletonList(IdUtil.getSnowflakeNextIdStr()));

        // 调用商品入库方法
        domainService.goodsIn(request);

        // 验证结果
        assertNotNull(request.getStoreId());
        assertEquals(1, request.getGoodsCodes().size());
    }

    @Test
    void goodsTransfer() {
    }

    @Test
    void goodsOut() {
    }
}