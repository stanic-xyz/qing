package cn.chenyunlong.qing.workflow.repository;

import cn.chenyunlong.qing.workflow.entity.BpmApprovalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BpmApprovalRecordRepository extends JpaRepository<BpmApprovalRecord, Long> {}
