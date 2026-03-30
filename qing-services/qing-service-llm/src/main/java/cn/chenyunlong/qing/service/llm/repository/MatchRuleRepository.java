package cn.chenyunlong.qing.service.llm.repository;

import cn.chenyunlong.qing.service.llm.entity.MatchRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRuleRepository extends JpaRepository<MatchRule, Long> {
    List<MatchRule> findByIsActiveTrueOrderByPriorityDesc();
}