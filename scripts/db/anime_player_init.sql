-- 动漫播放器相关功能数据库初始化脚本
-- 包含播放进度、观看历史、收藏夹、评分评论等表结构

-- 播放进度表
CREATE TABLE IF NOT EXISTS anime_playback_progress (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    anime_id BIGINT NOT NULL COMMENT '动漫ID',
    episode_id BIGINT NOT NULL COMMENT '剧集ID',
    current_position BIGINT NOT NULL DEFAULT 0 COMMENT '当前播放位置(秒)',
    total_duration BIGINT NOT NULL DEFAULT 0 COMMENT '总时长(秒)',
    progress_percentage DECIMAL(5,2) NOT NULL DEFAULT 0.00 COMMENT '播放进度百分比',
    is_completed BOOLEAN NOT NULL DEFAULT FALSE COMMENT '是否已完成观看',
    last_watch_time DATETIME NOT NULL COMMENT '最后播放时间',
    play_count INT NOT NULL DEFAULT 0 COMMENT '播放次数',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    version INT NOT NULL DEFAULT 0 COMMENT '版本号',
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE COMMENT '是否删除',
    INDEX idx_user_episode (user_id, episode_id),
    INDEX idx_user_anime (user_id, anime_id),
    INDEX idx_last_watch_time (last_watch_time),
    INDEX idx_is_completed (is_completed)
) COMMENT='播放进度表';

-- 观看历史表
CREATE TABLE IF NOT EXISTS anime_watch_history (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    anime_id BIGINT NOT NULL COMMENT '动漫ID',
    episode_id BIGINT NOT NULL COMMENT '剧集ID',
    watch_time DATETIME NOT NULL COMMENT '观看时间',
    watch_duration BIGINT NOT NULL DEFAULT 0 COMMENT '观看时长(秒)',
    device_type VARCHAR(50) COMMENT '设备类型',
    ip_address VARCHAR(45) COMMENT 'IP地址',
    user_agent TEXT COMMENT '用户代理',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    version INT NOT NULL DEFAULT 0 COMMENT '版本号',
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE COMMENT '是否删除',
    INDEX idx_user_id (user_id),
    INDEX idx_anime_id (anime_id),
    INDEX idx_watch_time (watch_time),
    INDEX idx_user_anime (user_id, anime_id)
) COMMENT='观看历史表';

-- 收藏夹表
CREATE TABLE IF NOT EXISTS anime_favorite (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    anime_id BIGINT NOT NULL COMMENT '动漫ID',
    favorite_time DATETIME NOT NULL COMMENT '收藏时间',
    favorite_group VARCHAR(100) NOT NULL DEFAULT '默认分组' COMMENT '收藏分组',
    remark TEXT COMMENT '备注',
    is_public BOOLEAN NOT NULL DEFAULT FALSE COMMENT '是否公开',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    version INT NOT NULL DEFAULT 0 COMMENT '版本号',
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE COMMENT '是否删除',
    UNIQUE KEY uk_user_anime (user_id, anime_id),
    INDEX idx_user_id (user_id),
    INDEX idx_anime_id (anime_id),
    INDEX idx_favorite_group (user_id, favorite_group),
    INDEX idx_favorite_time (favorite_time)
) COMMENT='收藏夹表';

-- 评分评论表
CREATE TABLE IF NOT EXISTS anime_rating (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    anime_id BIGINT NOT NULL COMMENT '动漫ID',
    score INT NOT NULL COMMENT '评分(1-10)',
    comment TEXT COMMENT '评论内容',
    rating_time DATETIME NOT NULL COMMENT '评分时间',
    last_update_time DATETIME NOT NULL COMMENT '最后更新时间',
    is_anonymous BOOLEAN NOT NULL DEFAULT FALSE COMMENT '是否匿名',
    like_count INT NOT NULL DEFAULT 0 COMMENT '点赞数',
    is_public BOOLEAN NOT NULL DEFAULT TRUE COMMENT '是否公开',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    version INT NOT NULL DEFAULT 0 COMMENT '版本号',
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE COMMENT '是否删除',
    UNIQUE KEY uk_user_anime (user_id, anime_id),
    INDEX idx_anime_id (anime_id),
    INDEX idx_score (score),
    INDEX idx_rating_time (rating_time),
    INDEX idx_like_count (like_count),
    INDEX idx_is_public (is_public),
    CONSTRAINT chk_score CHECK (score >= 1 AND score <= 10)
) COMMENT='评分评论表';

-- 插入测试数据

-- 播放进度测试数据
INSERT INTO anime_playback_progress (id, user_id, anime_id, episode_id, current_position, total_duration, progress_percentage, is_completed, last_watch_time, play_count) VALUES
(1, 1001, 1, 1, 1200, 1440, 83.33, FALSE, '2024-01-15 20:30:00', 3),
(2, 1001, 1, 2, 1440, 1440, 100.00, TRUE, '2024-01-16 21:00:00', 1),
(3, 1002, 1, 1, 600, 1440, 41.67, FALSE, '2024-01-15 19:45:00', 1),
(4, 1001, 2, 1, 800, 1500, 53.33, FALSE, '2024-01-17 20:15:00', 2);

-- 观看历史测试数据
INSERT INTO anime_watch_history (id, user_id, anime_id, episode_id, watch_time, watch_duration, device_type, ip_address, user_agent) VALUES
(1, 1001, 1, 1, '2024-01-15 20:00:00', 1200, 'PC', '192.168.1.100', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36'),
(2, 1001, 1, 1, '2024-01-15 20:30:00', 240, 'PC', '192.168.1.100', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36'),
(3, 1001, 1, 2, '2024-01-16 21:00:00', 1440, 'PC', '192.168.1.100', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36'),
(4, 1002, 1, 1, '2024-01-15 19:45:00', 600, 'Mobile', '192.168.1.101', 'Mozilla/5.0 (iPhone; CPU iPhone OS 17_0 like Mac OS X)'),
(5, 1001, 2, 1, '2024-01-17 20:15:00', 800, 'PC', '192.168.1.100', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36');

-- 收藏夹测试数据
INSERT INTO anime_favorite (id, user_id, anime_id, favorite_time, favorite_group, remark, is_public) VALUES
(1, 1001, 1, '2024-01-15 18:00:00', '热血动漫', '非常好看的动漫，推荐！', TRUE),
(2, 1001, 2, '2024-01-17 19:00:00', '热血动漫', '画风很棒', FALSE),
(3, 1002, 1, '2024-01-15 19:30:00', '默认分组', '', TRUE),
(4, 1003, 1, '2024-01-16 10:00:00', '经典收藏', '经典之作', TRUE);

-- 评分评论测试数据
INSERT INTO anime_rating (id, user_id, anime_id, score, comment, rating_time, last_update_time, is_anonymous, like_count, is_public) VALUES
(1, 1001, 1, 9, '画面精美，剧情紧凑，非常值得一看！', '2024-01-15 22:00:00', '2024-01-15 22:00:00', FALSE, 15, TRUE),
(2, 1002, 1, 8, '整体不错，但是节奏稍微有点慢', '2024-01-15 23:30:00', '2024-01-15 23:30:00', FALSE, 8, TRUE),
(3, 1003, 1, 10, '神作！必看！', '2024-01-16 12:00:00', '2024-01-16 12:00:00', TRUE, 25, TRUE),
(4, 1001, 2, 7, '还可以，但不如第一部', '2024-01-17 22:00:00', '2024-01-17 22:00:00', FALSE, 3, TRUE),
(5, 1004, 1, 6, '个人觉得一般般', '2024-01-18 14:00:00', '2024-01-18 14:00:00', FALSE, 1, TRUE);

-- 创建视图：动漫统计信息
CREATE OR REPLACE VIEW anime_statistics AS
SELECT 
    a.anime_id,
    COUNT(DISTINCT f.user_id) as favorite_count,
    COUNT(DISTINCT r.user_id) as rating_count,
    ROUND(AVG(r.score), 2) as average_score,
    COUNT(DISTINCT h.user_id) as watch_count,
    SUM(h.watch_duration) as total_watch_duration
FROM (
    SELECT DISTINCT anime_id FROM anime_favorite
    UNION
    SELECT DISTINCT anime_id FROM anime_rating
    UNION
    SELECT DISTINCT anime_id FROM anime_watch_history
) a
LEFT JOIN anime_favorite f ON a.anime_id = f.anime_id AND f.is_deleted = FALSE
LEFT JOIN anime_rating r ON a.anime_id = r.anime_id AND r.is_deleted = FALSE AND r.is_public = TRUE
LEFT JOIN anime_watch_history h ON a.anime_id = h.anime_id AND h.is_deleted = FALSE
GROUP BY a.anime_id;

-- 创建视图：用户观看统计
CREATE OR REPLACE VIEW user_watch_statistics AS
SELECT 
    user_id,
    COUNT(DISTINCT anime_id) as watched_anime_count,
    COUNT(DISTINCT episode_id) as watched_episode_count,
    SUM(watch_duration) as total_watch_duration,
    COUNT(*) as total_watch_sessions
FROM anime_watch_history
WHERE is_deleted = FALSE
GROUP BY user_id;

COMMIT;