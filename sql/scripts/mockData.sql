/*
 * Copyright (c) 2019-2023  YunLong Chen
 * Project Qing is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 *
 */

-- 为了操作方便，我们再创建一个插入数据的存储过程
-- 创建插入数据的存储过程

-- 为了操作方便，我们再创建一个插入数据的存储过程
-- 创建插入数据的存储过程

truncate anime_info;
truncate anime_episode;
truncate anime_playlist;

DELIMITER
$$
DROP PROCEDURE IF EXISTS `insert_test_records` $$

CREATE PROCEDURE `insert_test_records`(IN years INT, IN animeCount INT (4))
BEGIN
    DECLARE
yearNum INT DEFAULT 0;
    DECLARE
animeId INT;
    DECLARE
startYear INT DEFAULT 1996;
    DECLARE
animeCountOfYear INT(4) ZEROFILL DEFAULT 1;
    DECLARE
listSize INT DEFAULT 4;
    DECLARE
listCursor int default 1;
    DECLARE
currentListId INT default 1;

    DECLARE
episodeCursor int default 1;
    DECLARE
episodeSize INT DEFAULT 20;
    DECLARE
currentEpisodeId INT default 1;

    SET
AUTOCOMMIT = 0;
    set
animeId = concat(startYear, animeCountOfYear);

select max(id) + 1
into animeId
from anime_info;

WHILE
yearNum < years
        DO
BEGIN
INSERT INTO `anime_info`(`id`, `name`, `district_name`, `cover_url`, `type_name`, `instruction`,
                         `other_name`, `author`,
                         `premiere_date`, `company`,
                         `play_status`, `plot_type`, `tags`, `official_website`, `play_heat`,
                         `original_name`, created_at, updated_at)
    value (animeId, concat('Thief', animeId), 'China',
                           'https://anime-1255705827.cos.ap-guangzhou.myqcloud.com/H3e0ebe3098c345e9af4d0a68fc601667H.jpg',
                           'TV',
                           'China',
                           concat('China', 100000), 'China', '1999-10-20', 'China Anime', '1',
                           'smile',
                           'smile', 'https://bangumi.chenyunlong.cn',
                           '10000', 'ONE PIECE', current_timestamp, current_timestamp);

#
播放列表名称复位到0
                set listCursor = 1;
                #
插入播放列表
                while listCursor <= listSize
                    DO
BEGIN
insert into anime_playlist(id, name, anime_id, description) value (currentListId, concat('播放列表', listCursor + 1), animeId, 'desc');

set
episodeCursor = 0;
                            while
episodeCursor < episodeSize
                                DO
BEGIN
insert into anime_episode(id, anime_id, list_id, name, status, uploader_id,
                          uploader_name, url,
                          order_no) value (currentEpisodeId,
                                                                                   animeId,
                                                                                   currentListId,
                                                                                   concat('第', episodeCursor + 1, '集'),
                                                                                   0,
                                                                                   1, 'stan',
                                                                                   'https://bangumi.chenyunlong.cn/',
                                                                                   episodeCursor);
set
episodeCursor = episodeCursor + 1;
                                        set
currentEpisodeId = currentEpisodeId + 1;
END;
END WHILE;
END;
                        set
listCursor = listCursor + 1;
                        set
currentListId = currentListId + 1;
END WHILE;
END;
commit;
set
animeId = animeId + 1;
            set
animeCountOfYear = animeCountOfYear + 1;
            SET
yearNum = yearNum + 1;
END WHILE;
commit;

SET
AUTOCOMMIT = 1;
END
$$
DELIMITER ; -- 改回默认的 MySQL delimiter：';'

call insert_test_records(10, 10);
