-- ===========================================
-- Qing 微服务数据库初始化脚本
-- ===========================================

-- 创建数据库（如果不存在）
-- CREATE DATABASE IF NOT EXISTS qing;

-- 设置时区
SET timezone = 'Asia/Shanghai';

-- 创建扩展
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pg_trgm";

-- 创建用户表（示例）
-- 注意：实际的表结构应该由 JPA/Hibernate 自动创建
-- 这里只是创建一些基础的序列和索引

-- 创建序列
CREATE SEQUENCE IF NOT EXISTS hibernate_sequence START 1;

-- 设置数据库编码
-- ALTER DATABASE qing SET client_encoding TO 'utf8';
-- ALTER DATABASE qing SET default_transaction_isolation TO 'read committed';
-- ALTER DATABASE qing SET timezone TO 'Asia/Shanghai';

-- 创建基础索引（可选）
-- 这些索引可以提高查询性能

-- 输出初始化完成信息
DO $$
BEGIN
    RAISE NOTICE 'Qing 数据库初始化完成！';
END $$;