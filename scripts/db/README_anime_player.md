# 动漫播放器功能说明

本文档介绍动漫播放器相关功能的数据库设计和API接口。

## 数据库表结构

### 1. 播放进度表 (anime_playback_progress)
存储用户观看动漫的播放进度信息。

**主要字段：**
- `user_id`: 用户ID
- `anime_id`: 动漫ID
- `episode_id`: 剧集ID
- `current_position`: 当前播放位置(秒)
- `total_duration`: 总时长(秒)
- `progress_percentage`: 播放进度百分比
- `is_completed`: 是否已完成观看
- `last_watch_time`: 最后播放时间
- `play_count`: 播放次数

### 2. 观看历史表 (anime_watch_history)
记录用户的观看历史记录。

**主要字段：**
- `user_id`: 用户ID
- `anime_id`: 动漫ID
- `episode_id`: 剧集ID
- `watch_time`: 观看时间
- `watch_duration`: 观看时长(秒)
- `device_type`: 设备类型
- `ip_address`: IP地址
- `user_agent`: 用户代理

### 3. 收藏夹表 (anime_favorite)
存储用户收藏的动漫信息。

**主要字段：**
- `user_id`: 用户ID
- `anime_id`: 动漫ID
- `favorite_time`: 收藏时间
- `favorite_group`: 收藏分组
- `remark`: 备注
- `is_public`: 是否公开

### 4. 评分评论表 (anime_rating)
存储用户对动漫的评分和评论。

**主要字段：**
- `user_id`: 用户ID
- `anime_id`: 动漫ID
- `score`: 评分(1-10)
- `comment`: 评论内容
- `rating_time`: 评分时间
- `is_anonymous`: 是否匿名
- `like_count`: 点赞数
- `is_public`: 是否公开

## API接口说明

### 播放进度相关接口

**控制器：** `PlaybackProgressController`

- `PUT /api/v1/playback-progress/update` - 更新播放进度
- `PUT /api/v1/playback-progress/complete/{episodeId}` - 标记剧集为已完成
- `PUT /api/v1/playback-progress/increment-play-count/{episodeId}` - 增加播放次数
- `GET /api/v1/playback-progress/episode/{episodeId}` - 获取播放进度
- `GET /api/v1/playback-progress/anime/{animeId}` - 获取动漫播放进度
- `GET /api/v1/playback-progress/recent` - 获取最近观看
- `GET /api/v1/playback-progress/completed/{animeId}` - 获取已完成剧集
- `DELETE /api/v1/playback-progress/episode/{episodeId}` - 删除播放进度
- `DELETE /api/v1/playback-progress/anime/{animeId}` - 删除动漫播放进度
- `PUT /api/v1/playback-progress/reset/{episodeId}` - 重置播放进度

### 观看历史相关接口

**控制器：** `WatchHistoryController`

- `POST /api/v1/watch-history/record` - 记录观看历史
- `PUT /api/v1/watch-history/update-duration/{historyId}` - 更新观看时长
- `GET /api/v1/watch-history/user` - 获取用户观看历史
- `GET /api/v1/watch-history/recent` - 获取最近观看历史
- `GET /api/v1/watch-history/anime/{animeId}` - 获取动漫观看历史
- `GET /api/v1/watch-history/time-range` - 按时间范围获取观看历史
- `GET /api/v1/watch-history/anime/{animeId}/count` - 获取动漫观看次数
- `GET /api/v1/watch-history/user/total-duration` - 获取用户总观看时长
- `DELETE /api/v1/watch-history/{historyId}` - 删除观看历史
- `DELETE /api/v1/watch-history/user/clear` - 清空用户观看历史

### 收藏夹相关接口

**控制器：** `FavoriteController`

- `POST /api/v1/favorites/add` - 添加收藏
- `DELETE /api/v1/favorites/remove/{animeId}` - 取消收藏
- `PUT /api/v1/favorites/group/{animeId}` - 更新收藏分组
- `PUT /api/v1/favorites/remark/{animeId}` - 更新收藏备注
- `PUT /api/v1/favorites/public-status/{animeId}` - 设置公开状态
- `GET /api/v1/favorites/user` - 获取用户收藏列表
- `GET /api/v1/favorites/user/group/{group}` - 按分组获取收藏
- `GET /api/v1/favorites/user/groups` - 获取收藏分组列表
- `GET /api/v1/favorites/check/{animeId}` - 检查收藏状态
- `GET /api/v1/favorites/anime/{animeId}/count` - 获取动漫收藏次数
- `GET /api/v1/favorites/detail/{animeId}` - 获取收藏详情

### 评分评论相关接口

**控制器：** `RatingController`

- `POST /api/v1/ratings/add` - 添加评分评论
- `PUT /api/v1/ratings/score/{animeId}` - 更新评分
- `PUT /api/v1/ratings/comment/{animeId}` - 更新评论
- `PUT /api/v1/ratings/like/{ratingId}` - 点赞评论
- `PUT /api/v1/ratings/unlike/{ratingId}` - 取消点赞
- `PUT /api/v1/ratings/anonymous/{animeId}` - 设置匿名状态
- `PUT /api/v1/ratings/public/{animeId}` - 设置公开状态
- `DELETE /api/v1/ratings/{animeId}` - 删除评分评论
- `GET /api/v1/ratings/anime/{animeId}` - 获取动漫的所有评分
- `GET /api/v1/ratings/user` - 获取用户的评分记录
- `GET /api/v1/ratings/user/anime/{animeId}` - 获取用户对某个动漫的评分
- `GET /api/v1/ratings/anime/{animeId}/average` - 计算动漫平均评分
- `GET /api/v1/ratings/anime/{animeId}/count` - 获取动漫评分次数
- `GET /api/v1/ratings/popular` - 获取热门评论
- `GET /api/v1/ratings/latest` - 获取最新评论
- `GET /api/v1/ratings/check/{animeId}` - 检查用户是否已评分

## 数据库初始化

执行以下SQL脚本来初始化数据库：

```bash
mysql -u username -p database_name < anime_player_init.sql
```

## 测试数据

脚本中包含了以下测试数据：

- 4条播放进度记录
- 5条观看历史记录
- 4条收藏记录
- 5条评分评论记录

## 统计视图

### anime_statistics
提供动漫的统计信息，包括：
- 收藏数量
- 评分数量
- 平均评分
- 观看人数
- 总观看时长

### user_watch_statistics
提供用户观看统计信息，包括：
- 观看动漫数量
- 观看剧集数量
- 总观看时长
- 总观看会话数

## 注意事项

1. 所有表都包含软删除字段 `is_deleted`
2. 使用乐观锁机制，包含 `version` 字段
3. 评分范围限制在1-10之间
4. 用户对同一动漫只能有一条收藏记录和一条评分记录
5. 播放进度按用户和剧集维度存储
6. 观看历史记录每次观看行为