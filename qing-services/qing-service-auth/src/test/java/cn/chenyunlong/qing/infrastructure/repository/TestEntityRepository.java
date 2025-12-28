package cn.chenyunlong.qing.infrastructure.repository;

import cn.chenyunlong.qing.infrastructure.repository.entity.TestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestEntityRepository extends JpaRepository<TestEntity, Long> {
}
