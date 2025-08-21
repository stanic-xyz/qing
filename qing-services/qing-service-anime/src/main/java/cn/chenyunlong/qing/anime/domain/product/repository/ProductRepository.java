package cn.chenyunlong.qing.anime.domain.product.repository;

import cn.chenyunlong.qing.anime.domain.product.Product;
import cn.chenyunlong.qing.domain.common.AggregateId;
import cn.chenyunlong.qing.domain.common.repository.BaseRepository;

public interface ProductRepository extends BaseRepository<Product, AggregateId> {
}
