package cn.chenyunlong.qing.workflow.repository;

import cn.chenyunlong.qing.workflow.entity.BpmProcessLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BpmProcessLogRepository extends JpaRepository<BpmProcessLog, Long> {}
