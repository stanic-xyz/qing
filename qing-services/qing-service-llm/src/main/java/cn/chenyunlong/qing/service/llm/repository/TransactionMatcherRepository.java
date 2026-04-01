package cn.chenyunlong.qing.service.llm.repository;

import cn.chenyunlong.qing.service.llm.entity.TransactionMatcher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionMatcherRepository extends JpaRepository<TransactionMatcher, Long> {
    List<TransactionMatcher> findByIsActiveTrueOrderByPriorityDesc();
}
