package cn.chenyunlong.codegen.example.domain.repository;

import cn.chenyunlong.codegen.example.domain.Product;
import cn.chenyunlong.qing.domain.common.AggregateId;
import cn.chenyunlong.qing.domain.common.repository.BaseRepository;

public interface ProductRepository extends BaseRepository<Product, AggregateId> {
}
