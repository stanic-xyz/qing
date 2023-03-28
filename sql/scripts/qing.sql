/*
 * Copyright (c) 2023  YunLong Chen
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

/*
 * Copyright (c) 2023  YunLong Chen
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

/*
 * Copyright (c) 2019-2022 YunLong Chen
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

/*
 * Copyright (c) 2019-2022 YunLong Chen
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

/*!40101 SET @OLD_CHARACTER_SET_CLIENT = @@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS = @@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION = @@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE = @@TIME_ZONE */;
/*!40103 SET TIME_ZONE = '+08:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0 */;
/*!40101 SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES = @@SQL_NOTES, SQL_NOTES = 0 */;

drop database if exists qing;
create database qing;

use qing;

DROP TABLE IF EXISTS `activity`;
/*!40101 SET @saved_cs_client = @@character_set_client */;

CREATE TABLE `activity`
(
    `activity_id`   bigint(20)   not null AUTO_INCREMENT,
    `activity_name` varchar(100) not null,
    `create_time`   datetime     not null default NOW() comment '创建时间',
    `update_time`   datetime     not null default NOW() comment '创建时间',
    `search_value`  varchar(20)  not null default '' comment '查询参数',
    `create_by`     varchar(255) not null default '' comment '创建人',
    `update_by`     varchar(255) not null default '' comment '最后更新人',
    `remark`        varchar(255) not null default '' comment '创建时间',
    PRIMARY KEY (`activity_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS anime_comment;
/*!40101 SET @saved_cs_client = @@character_set_client */;

CREATE TABLE `anime_comment`
(
    `id`           bigint(20)   not null AUTO_INCREMENT COMMENT '评论ID',
    `cid`          bigint(20)   not null COMMENT '番剧ID',
    `username`     varchar(255) not null COMMENT '评论者昵称',
    `content`      text         not null COMMENT '评论内容',
    `ip_address`   varchar(15)  null     DEFAULT NULL COMMENT 'ip地址',
    `create_time`  datetime     not null default NOW() comment '创建时间',
    `update_time`  datetime     not null default NOW() comment '创建时间',
    `search_value` varchar(20)  not null default '' comment '查询参数',
    `create_by`    varchar(255) not null default '' comment '创建人',
    `update_by`    varchar(255) not null default '' comment '最后更新人',
    `remark`       varchar(255) not null default '' comment '创建时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `anime_district`;
/*!40101 SET @saved_cs_client = @@character_set_client */;

CREATE TABLE `anime_district`
(
    `id`           bigint(20)   not null AUTO_INCREMENT COMMENT '主键ID',
    `name`         varchar(50)           DEFAULT NULL COMMENT '地区名称',
    `code`         varchar(10)           DEFAULT NULL COMMENT '地区编码',
    `description`  varchar(255)          DEFAULT NULL COMMENT '描述信息',
    `create_time`  datetime     not null default now() comment '创建时间',
    `update_time`  datetime     not null default NOW() comment '创建时间',
    `search_value` varchar(20)  not null default '' comment '查询参数',
    `create_by`    varchar(255) not null default '' comment '创建人',
    `update_by`    varchar(255) not null default '' comment '最后更新人',
    `remark`       varchar(255) not null default '' comment '创建时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 10
  DEFAULT CHARSET = utf8mb4,
  collate utf8mb4_general_ci;

LOCK TABLES `anime_district` WRITE;
/*!40000 ALTER TABLE `anime_district`
    DISABLE KEYS */;
INSERT INTO `anime_district`(`id`, `name`, `code`, `description`)
VALUES (1, '日本', 'jp', '日本地区'),
       (2, '中国', 'cn', '中国'),
       (3, '欧美', 'eu', '欧美地区');
/*!40000 ALTER TABLE `anime_district`
    ENABLE KEYS */;
UNLOCK TABLES;

DROP TABLE IF EXISTS `anime_feedback`;
/*!40101 SET @saved_cs_client = @@character_set_client */;

CREATE TABLE `anime_feedback`
(
    `id`                bigint(20)   not null AUTO_INCREMENT,
    `type`              int(11)               DEFAULT NULL COMMENT '反馈类型\n1、链接失效\n2、资源质量差\n3、集数缺失\n4、其他',
    `detail`            text         not null COMMENT '详细信息',
    `mid`               bigint(20)   not null COMMENT '番剧ID',
    `uid`               bigint(20)            DEFAULT NULL COMMENT '用户ID',
    `processing_status` int(11)      not null COMMENT '处理状态\n0、未处理\n1、处理中\n2、已处理',
    `create_time`       datetime     not null default NOW() comment '创建时间',
    `update_time`       datetime     not null default NOW() comment '创建时间',
    `search_value`      varchar(20)  not null default '' comment '查询参数',
    `create_by`         varchar(255) not null default '' comment '创建人',
    `update_by`         varchar(255) not null default '' comment '最后更新人',
    `remark`            varchar(255) not null default '' comment '创建时间',
    PRIMARY KEY (`id`)
) engine = InnoDB comment ='反馈信息'
  charset utf8mb4;

DROP TABLE IF EXISTS `anime_info`;
/*!40101 SET @saved_cs_client = @@character_set_client */;

create table anime_info
(
    id               bigint auto_increment primary key,
    name             varchar(255)                           not null,
    district_id      int                                    null,
    district_name    varchar(255)                           null comment '地区(日本、中国、欧美）',
    cover_url        varchar(255) default ''                not null comment '封面地址',
    type_id          int(10)                                null comment '类型信息',
    type_name        varchar(255)                           null comment 'OVA TV 剧场版(theater_version)',
    instruction      text                                   null comment '番剧介绍',
    other_name       varchar(255)                           null comment '其它名称',
    author           varchar(255)                           null comment '原作',
    premiere_date    date                                   null comment '首播时间',
    company          varchar(255)                           null comment '制作公司',
    play_status      varchar(255) default '0'               null comment '0 未播放 1 连载 2 完结',
    plot_type        varchar(255)                           null comment '剧情类型',
    tags             varchar(255)                           null comment '标签',
    official_website varchar(255)                           null comment '官方网站',
    play_heat        varchar(255)                           null comment '播放热度',
    original_name    varchar(255)                           null comment '原版名称',
    create_time      datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time      datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    search_value     varchar(20)  default ''                not null comment '查询参数',
    create_by        varchar(255) default ''                not null comment '创建人',
    update_by        varchar(255) default ''                not null comment '最后更新人',
    remark           varchar(255) default ''                not null comment '创建时间',
    order_no         int          default 0                 not null
) comment '番剧信息' charset = utf8mb4;

create index anime_info_premiere_date_index
    on anime_info (premiere_date);

DROP TABLE IF EXISTS `anime_menu`;
/*!40101 SET @saved_cs_client = @@character_set_client */;

CREATE TABLE `anime_menu`
(
    `id`           bigint(20)            DEFAULT NULL COMMENT 'id',
    `name`         varchar(30)  not null DEFAULT '' COMMENT '名称',
    `path`         varchar(100) not null DEFAULT '' COMMENT '路径',
    `create_time`  datetime     not null default NOW() comment '创建时间',
    `update_time`  datetime     not null default NOW() comment '创建时间',
    `search_value` varchar(20)  not null default '' comment '查询参数',
    `create_by`    varchar(255) not null default '' comment '创建人',
    `update_by`    varchar(255) not null default '' comment '最后更新人',
    `remark`       varchar(255) not null default '' comment '创建时间'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


drop table if exists anime_playlist;

create table anime_playlist
(
    `id`           bigint auto_increment comment '播放列表主键ID',
    `name`         nvarchar(255)              not null comment '播放列表名称',
    `anime_id`     bigint                     not null comment '所属动漫主键ID',
    `description`  text                       null comment '描述信息',
    `create_time`  datetime     default NOW() not null comment '创建时间',
    `update_time`  datetime     default NOW() not null comment '创建时间',
    `search_value` varchar(20)  default ''    not null comment '查询参数',
    `create_by`    varchar(255) default ''    not null comment '创建人',
    `update_by`    varchar(255) default ''    not null comment '最后更新人',
    `remark`       varchar(255) default ''    not null comment '创建时间',
    `order_no`     int          default 0     not null comment '排序号',
    constraint table_playlist_pk
        primary key (id)
);

DROP TABLE IF EXISTS `anime_episode`;
/*!40101 SET @saved_cs_client = @@character_set_client */;

CREATE TABLE `anime_episode`
(
    `id`            bigint(20)   not null AUTO_INCREMENT COMMENT '视频ID',
    `anime_id`      bigint       not null comment '所属动漫主键ID',
    `list_id`       bigint       not null COMMENT '列表ID',
    `name`          varchar(255) not null COMMENT '视频标题名称',
    `status`        int(11)               DEFAULT '0' COMMENT '视频状态，0正常',
    `uploader_name` varchar(100)          DEFAULT NULL COMMENT '上传者名称',
    `uploader_id`   bigint(20)            DEFAULT NULL COMMENT '上传用户ID',
    `url`           varchar(255) not null COMMENT '视频地址',
    `order_no`      int(11)      not null COMMENT '视频排序',
    `create_time`   datetime     not null default NOW() comment '创建时间',
    `update_time`   datetime     not null default NOW() comment '创建时间',
    `search_value`  varchar(20)  not null default '' comment '查询参数',
    `create_by`     varchar(255) not null default '' comment '创建人',
    `update_by`     varchar(255) not null default '' comment '最后更新人',
    `remark`        varchar(255) not null default '' comment '创建时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
    COMMENT
        ='播放视频';

DROP TABLE IF EXISTS `anime_list_episode`;
/*!40101 SET @saved_cs_client = @@character_set_client */;

CREATE TABLE `anime_list_episode`
(
    `id`           bigint                     not null AUTO_INCREMENT COMMENT '视频ID',
    `list_id`      bigint                     not null COMMENT '列表ID',
    `episode_id`   bigint                     not null COMMENT '视频ID',
    `create_time`  datetime     default NOW() not null comment '创建时间',
    `update_time`  datetime     default NOW() not null comment '创建时间',
    `search_value` varchar(20)  default ''    not null comment '查询参数',
    `create_by`    varchar(255) default ''    not null comment '创建人',
    `update_by`    varchar(255) default ''    not null comment '最后更新人',
    `remark`       varchar(255) default ''    not null comment '创建时间',
    `order_no`     int          default 0     not null comment '排序号',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
    COMMENT
        ='播放列表，视频中间表';

DROP TABLE IF EXISTS `anime_resource`;
/*!40101 SET @saved_cs_client = @@character_set_client */;

CREATE TABLE `anime_resource`
(
    `id`           bigint(20)   not null AUTO_INCREMENT,
    `mid`          bigint(20)   not null COMMENT '番剧ID',
    `name`         varchar(255) not null COMMENT '播放资源列表名称',
    `create_time`  datetime     not null default NOW() comment '创建时间',
    `update_time`  datetime     not null default NOW() comment '创建时间',
    `search_value` varchar(20)  not null default '' comment '查询参数',
    `create_by`    varchar(255) not null default '' comment '创建人',
    `update_by`    varchar(255) not null default '' comment '最后更新人',
    `remark`       varchar(255) not null default '' comment '创建时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
    COMMENT
        ='番剧播放资源';

DROP TABLE IF EXISTS `anime_recommend`;

CREATE TABLE `anime_recommend`
(
    `id`           bigint(20)   not null AUTO_INCREMENT,
    `aid`          bigint(20)   not null COMMENT '番剧ID',
    `reason`       varchar(255) not null COMMENT '推荐理由',
    `order_no`     int(11)      not null COMMENT '视频排序',
    `create_time`  datetime     not null default NOW() comment '创建时间',
    `update_time`  datetime     not null default NOW() comment '创建时间',
    `search_value` varchar(20)  not null default '' comment '查询参数',
    `create_by`    varchar(255) not null default '' comment '创建人',
    `update_by`    varchar(255) not null default '' comment '最后更新人',
    `remark`       varchar(255) not null default '' comment '创建时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
    COMMENT
        ='番剧播放资源';

DROP TABLE IF EXISTS `anime_type`;
/*!40101 SET @saved_cs_client = @@character_set_client */;

CREATE TABLE `anime_type`
(
    `id`           bigint(20)   not null AUTO_INCREMENT COMMENT '主键ID',
    `name`         varchar(10)  not null COMMENT '类型名称',
    `description`  text COMMENT '详细介绍信息',
    `order_no`     int(11)               DEFAULT NULL COMMENT '排序号',
    `create_time`  datetime     not null default NOW() comment '创建时间',
    `update_time`  datetime     not null default NOW() comment '创建时间',
    `search_value` varchar(20)  not null default '' comment '查询参数',
    `create_by`    varchar(255) not null default '' comment '创建人',
    `update_by`    varchar(255) not null default '' comment '最后更新人',
    `remark`       varchar(255) not null default '' comment '创建时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 69
  DEFAULT CHARSET = utf8;


--
-- Dumping data for table `anime_type`
--

LOCK TABLES `anime_type` WRITE;
/*!40000 ALTER TABLE `anime_type`
    DISABLE KEYS */;
INSERT INTO `anime_type`(`id`, `name`, `description`, `order_no`)
VALUES (1, '欢乐向', '欢乐向', 1),
       (2, '搞笑', '搞笑', 2),
       (3, '运动', '运动', 3),
       (4, '励志', '励志', 4),
       (5, '热血', '热血', 5),
       (6, '战斗', '战斗', 6),
       (7, '竞技', '竞技', 7),
       (8, '校园', '校园', 8),
       (9, '青春', '青春', 9),
       (10, '爱情', '爱情', 10),
       (11, '冒险', '冒险', 11),
       (12, '后宫', '后宫', 12),
       (13, '百合', '百合', 13),
       (14, '治愈', '治愈', 14),
       (15, '萝莉', '萝莉', 15),
       (39, '魔法', '魔法', 39),
       (40, '悬疑', '悬疑', 40),
       (41, '推理', '推理', 41),
       (42, '奇幻', '奇幻', 42),
       (43, '科幻', '科幻', 43),
       (44, '游戏', '游戏', 44),
       (45, '神魔', '神魔', 45),
       (46, '恐怖', '恐怖', 46),
       (47, '血腥', '血腥', 47),
       (48, '机战', '机战', 48),
       (49, '战争', '战争', 49),
       (50, '犯罪', '犯罪', 50),
       (51, '历史', '历史', 51),
       (52, '社会', '社会', 52),
       (53, '职场', '职场', 53),
       (54, '剧情', '剧情', 54),
       (55, '伪娘', '伪娘', 55),
       (56, '耽美', '耽美', 56),
       (57, '童年', '童年', 57),
       (58, '教育', '教育', 58),
       (59, '亲子', '亲子', 59),
       (60, '真人', '真人', 60),
       (61, '歌舞', '歌舞', 61),
       (62, '肉番', '肉番', 62),
       (63, '美少女', '美少女', 63),
       (64, '轻小说', '轻小说', 64),
       (65, '吸血鬼', '吸血鬼', 65),
       (66, '乙女向', '乙女向', 66),
       (67, '泡面番', '泡面番', 67),
       (68, '欢乐向', '欢乐向', 68);
/*!40000 ALTER TABLE `anime_type`
    ENABLE KEYS */;
UNLOCK TABLES;

DROP TABLE IF EXISTS `anime_version`;
/*!40101 SET @saved_cs_client = @@character_set_client */;

CREATE TABLE `anime_version`
(
    `vid`          bigint(20)   not null AUTO_INCREMENT COMMENT '自增ID',
    `code`         varchar(10)  not null,
    `name`         varchar(50)  not null COMMENT '名称',
    `description`  varchar(255)          DEFAULT NULL COMMENT '描述信息',
    `create_time`  datetime     not null default NOW() comment '创建时间',
    `update_time`  datetime     not null default NOW() comment '创建时间',
    `search_value` varchar(20)  not null default '' comment '查询参数',
    `create_by`    varchar(255) not null default '' comment '创建人',
    `update_by`    varchar(255) not null default '' comment '最后更新人',
    `remark`       varchar(255) not null default '' comment '创建时间',
    PRIMARY KEY (`vid`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 8
  DEFAULT CHARSET = utf8mb4;


--
-- Dumping data for table `anime_version`
--

LOCK TABLES `anime_version` WRITE;
/*!40000 ALTER TABLE `anime_version`
    DISABLE KEYS */;
INSERT INTO `anime_version`
VALUES (1, 'TV', 'TV版本', '描述信息', now(), now(), 'stan', '', '', ''),
       (2, 'D', '剧场版', '描述新城', now(), now(), 'stan', '', '', ''),
       (3, 'OVA', 'OVA', '描述信息', now(), now(), 'stan', '', '', ''),
       (4, 'WEB', '网络剧', '网络剧', now(), now(), 'stan', '', '', ''),
       (5, 'ST', '短片同人', '短片同人', now(), now(), 'stan', '', '', ''),
       (6, 'DA', '动态漫画', '动态漫画', now(), now(), 'stan', '', '', ''),
       (7, 'MOVIE', '电影', '电影', now(), now(), 'stan', '', '', '');
/*!40000 ALTER TABLE `anime_version`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `car`
--

DROP TABLE IF EXISTS `car`;
/*!40101 SET @saved_cs_client = @@character_set_client */;

CREATE TABLE `car`
(
    `id`           bigint(20)   not null AUTO_INCREMENT,
    `car_img`      varchar(255)          DEFAULT NULL,
    `car_number`   varchar(255)          DEFAULT NULL,
    `car_status`   int(11)               DEFAULT NULL,
    `user_userid`  bigint(20)            DEFAULT NULL,
    `create_time`  datetime     not null default NOW() comment '创建时间',
    `update_time`  datetime     not null default NOW() comment '创建时间',
    `search_value` varchar(20)  not null default '' comment '查询参数',
    `create_by`    varchar(255) not null default '' comment '创建人',
    `update_by`    varchar(255) not null default '' comment '最后更新人',
    `remark`       varchar(255) not null default '' comment '创建时间',
    PRIMARY KEY (`id`),
    KEY `username_index` (`user_userid`)
) ENGINE = MyISAM
  DEFAULT CHARSET = utf8mb4;

DROP TABLE IF EXISTS `course`;
/*!40101 SET @saved_cs_client = @@character_set_client */;

CREATE TABLE `course`
(
    `id`           bigint(20)   not null AUTO_INCREMENT,
    `course_name`  varchar(255)          DEFAULT NULL,
    `course_time`  date                  DEFAULT NULL,
    `car_id`       bigint(20)            DEFAULT NULL,
    `user_userid`  bigint(20)            DEFAULT NULL,
    `create_time`  datetime     not null default NOW() comment '创建时间',
    `update_time`  datetime     not null default NOW() comment '创建时间',
    `search_value` varchar(20)  not null default '' comment '查询参数',
    `create_by`    varchar(255) not null default '' comment '创建人',
    `update_by`    varchar(255) not null default '' comment '最后更新人',
    `remark`       varchar(255) not null default '' comment '创建时间',
    PRIMARY KEY (`id`),
    KEY `car_index` (`car_id`)
) ENGINE = MyISAM
  DEFAULT CHARSET = utf8mb4;

LOCK TABLES `course` WRITE;
/*!40000 ALTER TABLE `course`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `course`
    ENABLE KEYS */;
UNLOCK TABLES;

DROP TABLE IF EXISTS `permission`;
/*!40101 SET @saved_cs_client = @@character_set_client */;

CREATE TABLE `permission`
(
    `id`          bigint(20)   not null,
    `name`        varchar(32)  not null,
    `description` varchar(255) not null,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

LOCK TABLES `permission` WRITE;
/*!40000 ALTER TABLE `permission`
    DISABLE KEYS */;
INSERT INTO `permission`
VALUES (1, 'read', '读取'),
       (2, 'write', '写入');
/*!40000 ALTER TABLE `permission`
    ENABLE KEYS */;
UNLOCK TABLES;

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client = @@character_set_client */;

CREATE TABLE `role`
(
    `role_id`          bigint(20)   not null,
    `role_name`        varchar(255) not null,
    `role_description` varchar(255) not null,
    `create_time`      datetime     not null default NOW() comment '创建时间',
    `update_time`      datetime     not null default NOW() comment '创建时间',
    `search_value`     varchar(20)  not null default '' comment '查询参数',
    `create_by`        varchar(255) not null default '' comment '创建人',
    `update_by`        varchar(255) not null default '' comment '最后更新人',
    `remark`           varchar(255) not null default '' comment '创建时间',
    PRIMARY KEY (`role_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role`
    DISABLE KEYS */;
INSERT INTO `role`
VALUES (1, 'admin', '管理团', now(), now(), 'stan', '', '', ''),
       (2, 'user', '普通用户', now(), now(), 'stan', '', '', '');
/*!40000 ALTER TABLE `role`
    ENABLE KEYS */;
UNLOCK TABLES;

DROP TABLE IF EXISTS `role_permission`;
/*!40101 SET @saved_cs_client = @@character_set_client */;

CREATE TABLE `role_permission`
(
    `id`            bigint(20) not null,
    `role_id`       bigint(20) not null,
    `permission_id` bigint(20) not null,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

LOCK TABLES `role_permission` WRITE;
/*!40000 ALTER TABLE `role_permission`
    DISABLE KEYS */;
INSERT INTO `role_permission`
VALUES (1, 1, 1),
       (2, 1, 2),
       (3, 2, 1);
/*!40000 ALTER TABLE `role_permission`
    ENABLE KEYS */;
UNLOCK TABLES;

DROP TABLE IF EXISTS `school_info`;
/*!40101 SET @saved_cs_client = @@character_set_client */;

CREATE TABLE `school_info`
(
    `school_id`    bigint(20)   not null,
    `school_code`  varchar(255)          DEFAULT NULL,
    `school_name`  varchar(255)          DEFAULT NULL,
    `create_time`  datetime     not null default NOW() comment '创建时间',
    `update_time`  datetime     not null default NOW() comment '创建时间',
    `search_value` varchar(20)  not null default '' comment '查询参数',
    `create_by`    varchar(255) not null default '' comment '创建人',
    `update_by`    varchar(255) not null default '' comment '最后更新人',
    `remark`       varchar(255) not null default '' comment '创建时间',
    PRIMARY KEY (`school_id`)
) ENGINE = MyISAM
  DEFAULT CHARSET = utf8mb4;

DROP TABLE IF EXISTS `t_listen_port`;
/*!40101 SET @saved_cs_client = @@character_set_client */;

CREATE TABLE `t_listen_port`
(
    `listen_port`   int(11)               DEFAULT NULL,
    `port_describe` varchar(255)          DEFAULT NULL,
    `dest_ip`       varchar(255)          DEFAULT NULL,
    `dest_port`     int(11)               DEFAULT NULL,
    `on_start`      tinyint(1)            DEFAULT NULL,
    `port_type`     int(11)               DEFAULT NULL,
    `cert_path`     varchar(255)          DEFAULT NULL,
    `cert_password` varchar(255)          DEFAULT NULL,
    `gmt_create`    datetime              DEFAULT NULL,
    `gmt_modify`    datetime              DEFAULT NULL,
    `create_time`   datetime     not null default NOW() comment '创建时间',
    `update_time`   datetime     not null default NOW() comment '创建时间',
    `search_value`  varchar(20)  not null default '' comment '查询参数',
    `create_by`     varchar(255) not null default '' comment '创建人',
    `update_by`     varchar(255) not null default '' comment '最后更新人',
    `remark`        varchar(255) not null default '' comment '创建时间'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `t_sign_record`;
/*!40101 SET @saved_cs_client = @@character_set_client */;

CREATE TABLE `t_sign_record`
(
    `id`                  bigint(20)   not null AUTO_INCREMENT COMMENT '自增键',
    `user_id`             bigint(20)   not null COMMENT '索引，用户表的id',
    `date_month`          date         not null COMMENT '索引，月份，形如2019-02',
    `mask`                int(32)               DEFAULT NULL COMMENT '用户签到的数据',
    `continue_sign_month` int(11)               DEFAULT '0' COMMENT '连续签到天数',
    `create_time`         datetime     not null default NOW() comment '创建时间',
    `update_time`         datetime     not null default NOW() comment '创建时间',
    `search_value`        varchar(20)  not null default '' comment '查询参数',
    `create_by`           varchar(255) not null default '' comment '创建人',
    `update_by`           varchar(255) not null default '' comment '最后更新人',
    `remark`              varchar(255) not null default '' comment '创建时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `upload_file`;
/*!40101 SET @saved_cs_client = @@character_set_client */;

CREATE TABLE `upload_file`
(
    `file_id`      bigint(20)   not null AUTO_INCREMENT,
    `file_name`    varchar(255)          DEFAULT NULL,
    `file_size`    bigint(20)            DEFAULT NULL,
    `mime_type`    varchar(255)          DEFAULT NULL,
    `url`          varchar(255)          DEFAULT NULL,
    `create_time`  datetime     not null default NOW() comment '创建时间',
    `update_time`  datetime     not null default NOW() comment '创建时间',
    `search_value` varchar(20)  not null default '' comment '查询参数',
    `create_by`    varchar(255) not null default '' comment '创建人',
    `update_by`    varchar(255) not null default '' comment '最后更新人',
    `remark`       varchar(255) not null default '' comment '创建时间',

    PRIMARY KEY (`file_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 138
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client = @@character_set_client */;

CREATE TABLE `user`
(
    `userid`       bigint(20)    not null AUTO_INCREMENT,
    `username`     varchar(10)            DEFAULT NULL COMMENT '用户登录的账号，长度为十位字符',
    `nickname`     nvarchar(20)  not null UNIQUE COMMENT '用户界面上展示的昵称',
    `avatar`       varchar(255)  not null DEFAULT '' COMMENT '用户的头像地址',
    `description`  nvarchar(200) not null DEFAULT '' COMMENT '个人简介',
    `email`        varchar(255)           DEFAULT NULL,
    `open_id`      varchar(30)            DEFAULT NULL,
    `password`     varchar(255)           DEFAULT NULL,
    `phone`        varchar(255)           DEFAULT NULL,
    `create_time`  datetime      not null default NOW() comment '创建时间',
    `update_time`  datetime      not null default NOW() comment '创建时间',
    `search_value` varchar(20)   not null default '' comment '查询参数',
    `create_by`    varchar(255)  not null default '' comment '创建人',
    `update_by`    varchar(255)  not null default '' comment '最后更新人',
    `remark`       varchar(255)  not null default '' comment '创建时间',
    PRIMARY KEY (`userid`),
    UNIQUE KEY `username_index` (`username`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 124
  DEFAULT CHARSET = utf8;


DROP TABLE IF EXISTS `user_third`;

CREATE TABLE `user_third`
(
    `id`            bigint(20)   not null AUTO_INCREMENT,
    `userid`        bigint(20)   not null comment '账号ID',
    `uid`           varchar(10)           DEFAULT NULL COMMENT '用户登录的账号，长度为十位字符',
    `app_type`      varchar(30)  not null comment '应用类型',
    `access_token`  nvarchar(20) not null UNIQUE COMMENT '用户界面上展示的昵称',
    `access_expire` varchar(255) not null DEFAULT '' COMMENT '用户的头像地址',
    `create_time`   datetime     not null default NOW() comment '创建时间',
    `update_time`   datetime     not null default NOW() comment '创建时间',
    `search_value`  varchar(20)  not null default '' comment '查询参数',
    `create_by`     varchar(255) not null default '' comment '创建人',
    `update_by`     varchar(255) not null default '' comment '最后更新人',
    `remark`        varchar(255) not null default '' comment '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `username_index` (`uid`, `app_type`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 124
  DEFAULT CHARSET = utf8;

# 导入默认的用户信息
INSERT INTO `user`
VALUES (123, 'stan', '纯纯的黑色幽默', 'avatar_path', 'description', '1576302867@qq.com',
        '13628091432', '$2a$10$45ScSS1BeuYizV2QYJ9HVOfpBoTOxnjXyCkNPFkTJnf9o3bW0l.4G',
        '13628091432', now(), now(), 'stan', '', '', '');

DROP TABLE IF EXISTS `user_role`;
/*!40101 SET @saved_cs_client = @@character_set_client */;

CREATE TABLE `user_role`
(
    `id`           bigint(20)   not null,
    `role_id`      bigint(20)   not null,
    `user_id`      bigint(20)   not null,
    `create_time`  datetime     not null default NOW() comment '创建时间',
    `update_time`  datetime     not null default NOW() comment '创建时间',
    `search_value` varchar(20)  not null default '' comment '查询参数',
    `create_by`    varchar(255) not null default '' comment '创建人',
    `update_by`    varchar(255) not null default '' comment '最后更新人',
    `remark`       varchar(255) not null default '' comment '创建时间'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `wechat_content`;
/*!40101 SET @saved_cs_client = @@character_set_client */;

CREATE TABLE `wechat_content`
(
    `id`               bigint(20)   not null AUTO_INCREMENT,
    `x_create_userid`  bigint(20)   not null,
    `x_last_edit_time` bigint(20)   not null,
    `x_create_time`    bigint(20)   not null,
    `x_data_flag`      bit(1)       not null,
    `content`          varchar(255)          DEFAULT NULL,
    `head_img_url`     varchar(255)          DEFAULT NULL,
    `message_type`     int(11)               DEFAULT NULL,
    `user_id`          bigint(20)   not null,
    `username`         varchar(255)          DEFAULT NULL,
    `create_time`      datetime     not null default NOW() comment '创建时间',
    `update_time`      datetime     not null default NOW() comment '创建时间',
    `search_value`     varchar(20)  not null default '' comment '查询参数',
    `create_by`        varchar(255) not null default '' comment '创建人',
    `update_by`        varchar(255) not null default '' comment '最后更新人',
    `remark`           varchar(255) not null default '' comment '创建时间',
    PRIMARY KEY (`id`)
) ENGINE = MyISAM
  DEFAULT CHARSET = utf8mb4;

LOCK TABLES `wechat_content` WRITE;
/*!40000 ALTER TABLE `wechat_content`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `wechat_content`
    ENABLE KEYS */;
UNLOCK TABLES;

DROP TABLE IF EXISTS `wechat_content_wechat_images`;
/*!40101 SET @saved_cs_client = @@character_set_client */;

CREATE TABLE `wechat_content_wechat_images`
(
    `wechat_content_id` bigint(20)   not null,
    `wechat_images_id`  bigint(20)   not null,
    `create_time`       datetime     not null default NOW() comment '创建时间',
    `update_time`       datetime     not null default NOW() comment '创建时间',
    `search_value`      varchar(20)  not null default '' comment '查询参数',
    `create_by`         varchar(255) not null default '' comment '创建人',
    `update_by`         varchar(255) not null default '' comment '最后更新人',
    `remark`            varchar(255) not null default '' comment '创建时间',
    UNIQUE KEY message_index (`wechat_images_id`),
    KEY `content_id_index` (`wechat_content_id`)
) ENGINE = MyISAM
  DEFAULT CHARSET = utf8mb4;

DROP TABLE IF EXISTS `wechat_images`;
/*!40101 SET @saved_cs_client = @@character_set_client */;

CREATE TABLE `wechat_images`
(
    `id`                bigint(20)   not null AUTO_INCREMENT,
    `x_create_userid`   bigint(20)   not null,
    `x_last_edit_time`  bigint(20)   not null,
    `x_create_time`     bigint(20)   not null,
    `x_data_flag`       bit(1)       not null,
    `image_url`         varchar(255)          DEFAULT NULL,
    `image_name`        varchar(255)          DEFAULT NULL,
    `wechat_content_id` bigint(20)            DEFAULT NULL,
    `create_time`       datetime     not null default NOW() comment '创建时间',
    `update_time`       datetime     not null default NOW() comment '创建时间',
    `search_value`      varchar(20)  not null default '' comment '查询参数',
    `create_by`         varchar(255) not null default '' comment '创建人',
    `update_by`         varchar(255) not null default '' comment '最后更新人',
    `remark`            varchar(255) not null default '' comment '创建时间',
    `order_no`          int                   default 0 not null comment '排序号',
    PRIMARY KEY (`id`),
    KEY `content_id_index` (`wechat_content_id`)
) ENGINE = MyISAM
  DEFAULT CHARSET = utf8mb4;


DROP TABLE IF EXISTS `bilibili_anime`;
/*!40101 SET @saved_cs_client = @@character_set_client */;

CREATE TABLE `bilibili_anime`
(
    `id`           bigint       not null AUTO_INCREMENT,
    `media_id`     bigint(20)   not null,
    `title`        varchar(255) not null default '',
    `season_id`    bigint(20)   not null default 0,
    `cover`        varchar(255) not null default '',
    `is_finished`  int          not null DEFAULT 0,
    `score`        double       not null default 0,
    `index_show`   varchar(255) not null default '',
    `link`         varchar(255) not null default '',
    `create_time`  datetime     not null default NOW() comment '创建时间',
    `update_time`  datetime     not null default NOW() comment '创建时间',
    `search_value` varchar(20)  not null default '' comment '查询参数',
    `create_by`    varchar(255) not null default '' comment '创建人',
    `update_by`    varchar(255) not null default '' comment '最后更新人',
    `remark`       varchar(255) not null default '' comment '创建时间',
    `order_no`     int                   default 0 not null comment '排序号',
    PRIMARY KEY (`id`),
    KEY `content_id_index` (`media_id`)
) ENGINE = MyISAM
  DEFAULT CHARSET = utf8mb4;

DROP TABLE IF EXISTS `bilibili_anime_score`;
create table `bilibili_anime_score`
(
    `id`           bigint auto_increment,
    `anime_id`     bigint       null comment '动漫ID',
    `score`        double                default 0 not null,
    `record_time`  datetime              default now() not null comment '记录时间',
    `create_time`  datetime     not null default NOW() comment '创建时间',
    `update_time`  datetime     not null default NOW() comment '创建时间',
    `search_value` varchar(20)  not null default '' comment '查询参数',
    `create_by`    varchar(255) not null default '' comment '创建人',
    `update_by`    varchar(255) not null default '' comment '最后更新人',
    `remark`       varchar(255) not null default '' comment '创建时间',
    `order_no`     int                   default 0 not null comment '排序号',
    constraint anime_score_pk primary key (id)

);

-- ----------------------------
-- 13、参数配置表
-- ----------------------------
drop table if exists sys_config;
CREATE TABLE sys_config
(
    config_id    INT(5) NOT NULL AUTO_INCREMENT COMMENT '参数主键',
    config_name  VARCHAR(100) DEFAULT '' COMMENT '参数名称',
    config_key   VARCHAR(100) DEFAULT '' COMMENT '参数键名',
    config_value VARCHAR(500) DEFAULT '' COMMENT '参数键值',
    config_type  CHAR(1)      DEFAULT 'N' COMMENT '系统内置（Y是 N否）',
    create_by    VARCHAR(64)  DEFAULT '' COMMENT '创建者',
    create_time  DATETIME COMMENT '创建时间',
    update_by    VARCHAR(64)  DEFAULT '' COMMENT '更新者',
    update_time  DATETIME COMMENT '更新时间',
    remark       VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (config_id)
) ENGINE = INNODB
  char set utf8mb4
  AUTO_INCREMENT = 100 COMMENT ='参数配置表';


insert into sys_config
values (1, '主框架页-默认皮肤样式名称', 'sys.index.skinName', 'skin-blue', 'Y', 'admin', sysdate(), '', null,
        '蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow');
insert into sys_config
values (2, '用户管理-账号初始密码', 'sys.user.initPassword', '123456', 'Y', 'admin', sysdate(), '', null,
        '初始化密码 123456');
insert into sys_config
values (3, '主框架页-侧边栏主题', 'sys.index.sideTheme', 'theme-dark', 'Y', 'admin', sysdate(), '', null,
        '深黑主题theme-dark，浅色主题theme-light，深蓝主题theme-blue');
insert into sys_config
values (4, '账号自助-是否开启用户注册功能', 'sys.account.registerUser', 'false', 'Y', 'admin', sysdate(), '', null,
        '是否开启注册用户功能（true开启，false关闭）');
insert into sys_config
values (5, '用户管理-密码字符范围', 'sys.account.chrtype', '0', 'Y', 'admin', sysdate(), '', null,
        '默认任意字符范围，0任意（密码可以输入任意字符），1数字（密码只能为0-9数字），2英文字母（密码只能为a-z和A-Z字母），3字母和数字（密码必须包含字母，数字）,4字母数字和特殊字符（目前支持的特殊字符包括：~!@#$%^&*()-=_+）');
insert into sys_config
values (6, '用户管理-初始密码修改策略', 'sys.account.initPasswordModify', '0', 'Y', 'admin', sysdate(), '', null,
        '0：初始密码修改策略关闭，没有任何提示，1：提醒用户，如果未修改初始密码，则在登录时就会提醒修改密码对话框');
insert into sys_config
values (7, '用户管理-账号密码更新周期', 'sys.account.passwordValidateDays', '0', 'Y', 'admin', sysdate(), '', null,
        '密码更新周期（填写数字，数据初始化值为0不限制，若修改必须为大于0小于365的正整数），如果超过这个周期登录系统时，则在登录时就会提醒修改密码对话框');
insert into sys_config
values (8, '主框架页-菜单导航显示风格', 'sys.index.menuStyle', 'default', 'Y', 'admin', sysdate(), '', null,
        '菜单导航显示风格（default为左侧导航菜单，topnav为顶部导航菜单）');
insert into sys_config
values (9, '主框架页-是否开启页脚', 'sys.index.ignoreFooter', 'true', 'Y', 'admin', sysdate(), '', null,
        '是否开启底部页脚显示（true显示，false隐藏）');
insert into sys_config
values (10, '主框架页-是否使用cdn加速', 'sys.content.cdn', 'true', 'Y', 'admin', sysdate(), '', null,
        'cdn地址');


-- ----------------------------
-- 16、定时任务调度表
-- ----------------------------
drop table if exists sys_job;
create table sys_job
(
    job_id          bigint(20)   not null auto_increment comment '任务ID',
    job_name        varchar(64)  default '' comment '任务名称',
    job_group       varchar(64)  default 'DEFAULT' comment '任务组名',
    invoke_target   varchar(500) not null comment '调用目标字符串',
    cron_expression varchar(255) default '' comment 'cron执行表达式',
    misfire_policy  varchar(20)  default '3' comment '计划执行错误策略（1立即执行 2执行一次 3放弃执行）',
    concurrent      char(1)      default '1' comment '是否并发执行（0允许 1禁止）',
    status          char(1)      default '0' comment '状态（0正常 1暂停）',
    create_by       varchar(64)  default '' comment '创建者',
    create_time     datetime comment '创建时间',
    update_by       varchar(64)  default '' comment '更新者',
    update_time     datetime comment '更新时间',
    remark          varchar(500) default '' comment '备注信息',
    primary key (job_id, job_name, job_group)
) engine = innodb
  char set utf8mb4
  auto_increment = 100 comment = '定时任务调度表';


-- ----------------------------
-- 17、定时任务调度日志表
-- ----------------------------
drop table if exists sys_job_log;
create table sys_job_log
(
    job_log_id     bigint(20)   not null auto_increment comment '任务日志ID',
    job_name       varchar(64)  not null comment '任务名称',
    job_group      varchar(64)  not null comment '任务组名',
    invoke_target  varchar(500) not null comment '调用目标字符串',
    job_message    varchar(500) comment '日志信息',
    status         char(1)       default '0' comment '执行状态（0正常 1失败）',
    exception_info varchar(2000) default '' comment '异常信息',
    create_time    datetime comment '创建时间',
    primary key (job_log_id)
) engine = innodb comment = '定时任务调度日志表'
  char set utf8mb4
  collate utf8mb4_general_ci;


-- ----------------------------
-- 1、存储每一个已配置的 jobDetail 的详细信息
-- ----------------------------
drop table if exists QRTZ_JOB_DETAILS;
create table QRTZ_JOB_DETAILS
(
    sched_name        varchar(120) not null,
    job_name          varchar(200) not null,
    job_group         varchar(200) not null,
    `description`     varchar(250) null,
    job_class_name    varchar(250) not null,
    is_durable        varchar(1)   not null,
    is_nonconcurrent  varchar(1)   not null,
    is_update_data    varchar(1)   not null,
    requests_recovery varchar(1)   not null,
    job_data          blob         null,
    primary key (sched_name, job_name, job_group)
) engine = innodb;

-- ----------------------------
-- 2、 存储已配置的 Trigger 的信息
-- ----------------------------
drop table if exists QRTZ_TRIGGERS;
create table QRTZ_TRIGGERS
(
    sched_name     varchar(120) not null,
    trigger_name   varchar(200) not null,
    trigger_group  varchar(200) not null,
    job_name       varchar(200) not null,
    job_group      varchar(200) not null,
    description    varchar(250) null,
    next_fire_time bigint(13)   null,
    prev_fire_time bigint(13)   null,
    priority       integer      null,
    trigger_state  varchar(16)  not null,
    trigger_type   varchar(8)   not null,
    start_time     bigint(13)   not null,
    end_time       bigint(13)   null,
    calendar_name  varchar(200) null,
    misfire_instr  smallint(2)  null,
    job_data       blob         null,
    primary key (sched_name, trigger_name, trigger_group),
    foreign key (sched_name, job_name, job_group) references QRTZ_JOB_DETAILS (sched_name, job_name, job_group)
) engine = innodb;

-- ----------------------------
-- 3、 存储简单的 Trigger，包括重复次数，间隔，以及已触发的次数
-- ----------------------------
drop table if exists QRTZ_SIMPLE_TRIGGERS;
create table QRTZ_SIMPLE_TRIGGERS
(
    sched_name      varchar(120) not null,
    trigger_name    varchar(200) not null,
    trigger_group   varchar(200) not null,
    repeat_count    bigint(7)    not null,
    repeat_interval bigint(12)   not null,
    times_triggered bigint(10)   not null,
    primary key (sched_name, trigger_name, trigger_group),
    foreign key (sched_name, trigger_name, trigger_group) references QRTZ_TRIGGERS (sched_name, trigger_name, trigger_group)
) engine = innodb;

-- ----------------------------
-- 4、 存储 Cron Trigger，包括 Cron 表达式和时区信息
-- ----------------------------
drop table if exists QRTZ_CRON_TRIGGERS;
create table QRTZ_CRON_TRIGGERS
(
    sched_name      varchar(120) not null,
    trigger_name    varchar(200) not null,
    trigger_group   varchar(200) not null,
    cron_expression varchar(200) not null,
    time_zone_id    varchar(80),
    primary key (sched_name, trigger_name, trigger_group),
    foreign key (sched_name, trigger_name, trigger_group) references QRTZ_TRIGGERS (sched_name, trigger_name, trigger_group)
) engine = innodb;

-- ----------------------------
-- 5、 Trigger 作为 Blob 类型存储(用于 Quartz 用户用 JDBC 创建他们自己定制的 Trigger 类型，JobStore 并不知道如何存储实例的时候)
-- ----------------------------
drop table if exists QRTZ_BLOB_TRIGGERS;
create table QRTZ_BLOB_TRIGGERS
(
    sched_name    varchar(120) not null,
    trigger_name  varchar(200) not null,
    trigger_group varchar(200) not null,
    blob_data     blob         null,
    primary key (sched_name, trigger_name, trigger_group),
    foreign key (sched_name, trigger_name, trigger_group) references QRTZ_TRIGGERS (sched_name, trigger_name, trigger_group)
) engine = innodb;

-- ----------------------------
-- 6、 以 Blob 类型存储存放日历信息， quartz可配置一个日历来指定一个时间范围
-- ----------------------------
drop table if exists QRTZ_CALENDARS;
create table QRTZ_CALENDARS
(
    sched_name    varchar(120) not null,
    calendar_name varchar(200) not null,
    calendar      blob         not null,
    primary key (sched_name, calendar_name)
) engine = innodb;

-- ----------------------------
-- 7、 存储已暂停的 Trigger 组的信息
-- ----------------------------
drop table if exists QRTZ_PAUSED_TRIGGER_GRPS;
create table QRTZ_PAUSED_TRIGGER_GRPS
(
    sched_name    varchar(120) not null,
    trigger_group varchar(200) not null,
    primary key (sched_name, trigger_group)
) engine = innodb;

-- ----------------------------
-- 8、 存储与已触发的 Trigger 相关的状态信息，以及相联 Job 的执行信息
-- ----------------------------
drop table if exists QRTZ_FIRED_TRIGGERS;
create table QRTZ_FIRED_TRIGGERS
(
    sched_name        varchar(120) not null,
    entry_id          varchar(95)  not null,
    trigger_name      varchar(200) not null,
    trigger_group     varchar(200) not null,
    instance_name     varchar(200) not null,
    fired_time        bigint(13)   not null,
    sched_time        bigint(13)   not null,
    priority          integer      not null,
    state             varchar(16)  not null,
    job_name          varchar(200) null,
    job_group         varchar(200) null,
    is_nonconcurrent  varchar(1)   null,
    requests_recovery varchar(1)   null,
    primary key (sched_name, entry_id)
) engine = innodb;

-- ----------------------------
-- 9、 存储少量的有关 Scheduler 的状态信息，假如是用于集群中，可以看到其他的 Scheduler 实例
-- ----------------------------
drop table if exists QRTZ_SCHEDULER_STATE;
create table QRTZ_SCHEDULER_STATE
(
    sched_name        varchar(120) not null,
    instance_name     varchar(200) not null,
    last_checkin_time bigint(13)   not null,
    checkin_interval  bigint(13)   not null,
    primary key (sched_name, instance_name)
) engine = innodb;

-- ----------------------------
-- 10、 存储程序的悲观锁的信息(假如使用了悲观锁)
-- ----------------------------
drop table if exists QRTZ_LOCKS;
create table QRTZ_LOCKS
(
    sched_name varchar(120) not null,
    lock_name  varchar(40)  not null,
    primary key (sched_name, lock_name)
) engine = innodb;

drop table if exists QRTZ_SIMPROP_TRIGGERS;
create table QRTZ_SIMPROP_TRIGGERS
(
    sched_name    varchar(120)   not null,
    trigger_name  varchar(200)   not null,
    trigger_group varchar(200)   not null,
    str_prop_1    varchar(512)   null,
    str_prop_2    varchar(512)   null,
    str_prop_3    varchar(512)   null,
    int_prop_1    int            null,
    int_prop_2    int            null,
    long_prop_1   bigint         null,
    long_prop_2   bigint         null,
    dec_prop_1    numeric(13, 4) null,
    dec_prop_2    numeric(13, 4) null,
    bool_prop_1   varchar(1)     null,
    bool_prop_2   varchar(1)     null,
    primary key (sched_name, trigger_name, trigger_group),
    foreign key (sched_name, trigger_name, trigger_group) references QRTZ_TRIGGERS (sched_name, trigger_name, trigger_group)
) engine = innodb;


/*!40103 SET TIME_ZONE = @OLD_TIME_ZONE */;
/*!40101 SET SQL_MODE = @OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT = @OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS = @OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION = @OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES = @OLD_SQL_NOTES */;
