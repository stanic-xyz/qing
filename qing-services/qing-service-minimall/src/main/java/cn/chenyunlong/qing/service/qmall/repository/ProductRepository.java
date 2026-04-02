package cn.chenyunlong.qing.service.qmall.repository;

import cn.chenyunlong.qing.service.qmall.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // 可选：使用悲观锁（示例），实际我们使用乐观锁（@Version），因此无需此方法
    // @Lock(LockModeType.PESSIMISTIC_WRITE)
    // @Query("select p from Product p where p.id = :id")
    // Optional<Product> findByIdWithLock(@Param("id") Long id);
}
