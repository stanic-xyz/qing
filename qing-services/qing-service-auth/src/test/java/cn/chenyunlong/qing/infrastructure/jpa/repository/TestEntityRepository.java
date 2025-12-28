package cn.chenyunlong.qing.infrastructure.jpa.repository;

import cn.chenyunlong.qing.infrastructure.jpa.entity.TestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestEntityRepository extends JpaRepository<TestEntity, Long> {
}
