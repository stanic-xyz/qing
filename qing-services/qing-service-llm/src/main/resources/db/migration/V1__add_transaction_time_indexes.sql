-- ============================================
-- Dashboard 性能优化：添加 transaction_time 索引
-- 执行时间：业务低峰期
-- 影响：略微增加写操作时间，显著提升读性能
-- ============================================

-- 为交易记录表添加 transaction_time 索引（如果不存在）
CREATE INDEX IF NOT EXISTS idx_finance_transaction_record_transaction_time
ON finance_transaction_record(transaction_time);

-- 为交易记录表添加复合索引（时间 + 软删除标记）
CREATE INDEX IF NOT EXISTS idx_finance_transaction_record_time_deleted
ON finance_transaction_record(transaction_time, is_deleted);

-- 为交易记录表添加复合索引（时间 + 是否导入 + 软删除标记）
-- 覆盖 Dashboard 统计查询的常用场景
CREATE INDEX IF NOT EXISTS idx_finance_transaction_record_time_import_deleted
ON finance_transaction_record(transaction_time, is_imported, is_deleted);

-- 为交易记录表添加复合索引（时间 + 交易类型 + 软删除标记）
-- 用于按月/按日统计收支的场景
CREATE INDEX IF NOT EXISTS idx_finance_transaction_record_time_type_deleted
ON finance_transaction_record(transaction_time, transaction_type, is_deleted);

-- 验证索引是否创建成功
-- SELECT indexname, indexdef FROM pg_indexes WHERE tablename = 'finance_transaction_record';
