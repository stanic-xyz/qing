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

--
-- Table structure for table `activity`
--

DROP TABLE IF EXISTS `activity`;
/*!40101 SET @saved_cs_client = @@character_set_client */;

CREATE TABLE `activity`
(
    `activity_id`   bigint(20)   NOT NULL AUTO_INCREMENT,
    `activity_name` varchar(100) NOT NULL,
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
    `id`           bigint(20)   NOT NULL AUTO_INCREMENT COMMENT '评论ID',
    `cid`          bigint(20)   NOT NULL COMMENT '番剧ID',
    `username`     varchar(255) not null DEFAULT NULL COMMENT '评论者昵称',
    `content`      text         NOT NULL COMMENT '评论内容',
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

--
-- Table structure for table `anime_district`
--

DROP TABLE IF EXISTS `anime_district`;
/*!40101 SET @saved_cs_client = @@character_set_client */;

CREATE TABLE `anime_district`
(
    `id`           bigint(20)   NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name`         varchar(50)                                             DEFAULT NULL COMMENT '地区名称',
    `code`         varchar(10)                                             DEFAULT NULL COMMENT '地区编码',
    `description`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '描述信息',
    `create_time`  datetime     not null                                   default NOW() comment '创建时间',
    `update_time`  datetime     not null                                   default NOW() comment '创建时间',
    `search_value` varchar(20)  not null                                   default '' comment '查询参数',
    `create_by`    varchar(255) not null                                   default '' comment '创建人',
    `update_by`    varchar(255) not null                                   default '' comment '最后更新人',
    `remark`       varchar(255) not null                                   default '' comment '创建时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 4
  DEFAULT CHARSET = utf8mb4;


--
-- Dumping data for table `anime_district`
--

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

--
-- Table structure for table `anime_episode`
--

DROP TABLE IF EXISTS `anime_episode`;
/*!40101 SET @saved_cs_client = @@character_set_client */;

CREATE TABLE `anime_episode`
(
    `id`            bigint(20)   NOT NULL AUTO_INCREMENT COMMENT '视频ID',
    `anime_id`      bigint(20)            DEFAULT NULL COMMENT '动漫ID',
    `name`          varchar(255) NOT NULL COMMENT '视频标题名称',
    `status`        int(11)               DEFAULT '0' COMMENT '视频状态，0正常',
    `uploader_name` varchar(100)          DEFAULT NULL COMMENT '上传者名称',
    `uploader_id`   bigint(20)            DEFAULT NULL COMMENT '上传用户ID',
    `upload_time`   datetime              DEFAULT NULL COMMENT '视频上传时间',
    `url1`          varchar(255) NOT NULL COMMENT '视频地址',
    `url3`          varchar(255)          DEFAULT NULL COMMENT '视频播放地址3',
    `url2`          varchar(255)          DEFAULT NULL COMMENT '视频播放地址2',
    `order_no`      int(11)      NOT NULL COMMENT '视频排序',
    `create_time`   datetime     not null default NOW() comment '创建时间',
    `update_time`   datetime     not null default NOW() comment '创建时间',
    `search_value`  varchar(20)  not null default '' comment '查询参数',
    `create_by`     varchar(255) not null default '' comment '创建人',
    `update_by`     varchar(255) not null default '' comment '最后更新人',
    `remark`        varchar(255) not null default '' comment '创建时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='视频内容';


--
-- Dumping data for table `anime_episode`
--

LOCK TABLES `anime_episode` WRITE;
/*!40000 ALTER TABLE `anime_episode`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `anime_episode`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `anime_feedback`
--

DROP TABLE IF EXISTS `anime_feedback`;
/*!40101 SET @saved_cs_client = @@character_set_client */;

CREATE TABLE `anime_feedback`
(
    `id`                bigint(20)   NOT NULL AUTO_INCREMENT,
    `type`              int(11)               DEFAULT NULL COMMENT '反馈类型\n1、链接失效\n2、资源质量差\n3、集数缺失\n4、其他',
    `detail`            text COMMENT '详细信息',
    `mid`               bigint(20)   NOT NULL COMMENT '番剧ID',
    `uid`               bigint(20)            DEFAULT NULL COMMENT '用户ID',
    `processing_status` int(11)      NOT NULL COMMENT '处理状态\n0、未处理\n1、处理中\n2、已处理',
    `create_time`       datetime     not null default NOW() comment '创建时间',
    `update_time`       datetime     not null default NOW() comment '创建时间',
    `search_value`      varchar(20)  not null default '' comment '查询参数',
    `create_by`         varchar(255) not null default '' comment '创建人',
    `update_by`         varchar(255) not null default '' comment '最后更新人',
    `remark`            varchar(255) not null default '' comment '创建时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='反馈信息';


--
-- Dumping data for table `anime_feedback`
--

LOCK TABLES `anime_feedback` WRITE;
/*!40000 ALTER TABLE `anime_feedback`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `anime_feedback`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `anime_info`
--

DROP TABLE IF EXISTS `anime_info`;
/*!40101 SET @saved_cs_client = @@character_set_client */;

CREATE TABLE `anime_info`
(
    `id`               bigint(20)   NOT NULL AUTO_INCREMENT,
    `name`             varchar(255) NOT NULL,
    `district`         varchar(255)          DEFAULT NULL COMMENT '地区(日本、中国、欧美）',
    `cover_url`        varchar(255) NOT NULL COMMENT '封面地址',
    `type`             varchar(255)          DEFAULT NULL COMMENT 'OVA\nTV\n剧场版(theater_version)',
    `instruction`      text COMMENT '番剧介绍',
    `other_name`       varchar(255)          DEFAULT NULL COMMENT '其它名称',
    `author`           varchar(255)          DEFAULT NULL COMMENT '原作',
    `premiere_date`    varchar(255)          DEFAULT NULL COMMENT '首播时间',
    `company`          varchar(255)          DEFAULT NULL COMMENT '制作公司',
    `play_status`      varchar(255)          DEFAULT '0' COMMENT '0 未播放\n1 连载\n2 完结',
    `plot_type`        varchar(255)          DEFAULT NULL COMMENT '剧情类型',
    `tags`             varchar(255)          DEFAULT NULL COMMENT '标签',
    `official_website` varchar(255)          DEFAULT NULL COMMENT '官方网站',
    `play_heat`        varchar(255)          DEFAULT NULL COMMENT '播放热度',
    `original_name`    varchar(255)          DEFAULT NULL COMMENT '原版名称',
    `create_time`      datetime     not null default NOW() comment '创建时间',
    `update_time`      datetime     not null default NOW() comment '创建时间',
    `search_value`     varchar(20)  not null default '' comment '查询参数',
    `create_by`        varchar(255) not null default '' comment '创建人',
    `update_by`        varchar(255) not null default '' comment '最后更新人',
    `remark`           varchar(255) not null default '' comment '创建时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 20210015
  DEFAULT CHARSET = utf8 COMMENT ='番剧信息';



--
-- Table structure for table `anime_menu`
--

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
    `name`         nvarchar(255) not null comment '播放列表名称',
    `anime_id`     bigint        not null comment '所属动漫主键ID',
    `description`  text          null comment '描述信息',
    `create_time`  datetime      not null default NOW() comment '创建时间',
    `update_time`  datetime      not null default NOW() comment '创建时间',
    `search_value` varchar(20)   not null default '' comment '查询参数',
    `create_by`    varchar(255)  not null default '' comment '创建人',
    `update_by`    varchar(255)  not null default '' comment '最后更新人',
    `remark`       varchar(255)  not null default '' comment '创建时间',
    constraint table_playlist_pk
        primary key (id)
);

--
-- Table structure for table `anime_resource`
--

DROP TABLE IF EXISTS `anime_resource`;
/*!40101 SET @saved_cs_client = @@character_set_client */;

CREATE TABLE `anime_resource`
(
    `id`           bigint(20)   NOT NULL AUTO_INCREMENT,
    `mid`          bigint(20)   NOT NULL COMMENT '番剧ID',
    `name`         varchar(255) NOT NULL COMMENT '播放资源列表名称',
    `create_time`  datetime     not null default NOW() comment '创建时间',
    `update_time`  datetime     not null default NOW() comment '创建时间',
    `search_value` varchar(20)  not null default '' comment '查询参数',
    `create_by`    varchar(255) not null default '' comment '创建人',
    `update_by`    varchar(255) not null default '' comment '最后更新人',
    `remark`       varchar(255) not null default '' comment '创建时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='番剧播放资源';

DROP TABLE IF EXISTS `anime_recommend`;

CREATE TABLE `anime_recommend`
(
    `id`          bigint(20)   NOT NULL AUTO_INCREMENT,
    `aid`         bigint(20)   NOT NULL COMMENT '番剧ID',
    `reason`      varchar(255) NOT NULL COMMENT '推荐理由',
    `create_time` datetime     NOT NULL,
    `order_no`    int          not null default 0 comment '排序ID',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='番剧播放资源';


--
-- Dumping data for table `anime_resource`
--

LOCK TABLES `anime_resource` WRITE;
/*!40000 ALTER TABLE `anime_resource`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `anime_resource`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `anime_type`
--

DROP TABLE IF EXISTS `anime_type`;
/*!40101 SET @saved_cs_client = @@character_set_client */;

CREATE TABLE `anime_type`
(
    `id`           bigint(20)   NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name`         varchar(10)  NOT NULL COMMENT '类型名称',
    `description`  text COMMENT '详细介绍信息',
    `order`        int(11)               DEFAULT NULL COMMENT '排序号',
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
INSERT INTO `anime_type`(`id`, `name`, `description`, `order`)
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

--
-- Table structure for table `anime_version`
--

DROP TABLE IF EXISTS `anime_version`;
/*!40101 SET @saved_cs_client = @@character_set_client */;

CREATE TABLE `anime_version`
(
    `vid`          bigint(20)   NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `code`         varchar(10)  NOT NULL,
    `name`         varchar(50)  NOT NULL COMMENT '名称',
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
    `id`           bigint(20)   NOT NULL AUTO_INCREMENT,
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


--
-- Dumping data for table `car`
--

LOCK TABLES `car` WRITE;
/*!40000 ALTER TABLE `car`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `car`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `course`
--

DROP TABLE IF EXISTS `course`;
/*!40101 SET @saved_cs_client = @@character_set_client */;

CREATE TABLE `course`
(
    `id`           bigint(20)   NOT NULL AUTO_INCREMENT,
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


--
-- Dumping data for table `course`
--

LOCK TABLES `course` WRITE;
/*!40000 ALTER TABLE `course`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `course`
    ENABLE KEYS */;
UNLOCK TABLES;


--
-- Table structure for table `permission`
--

DROP TABLE IF EXISTS `permission`;
/*!40101 SET @saved_cs_client = @@character_set_client */;

CREATE TABLE `permission`
(
    `id`          bigint(20)   NOT NULL,
    `name`        varchar(32)  NOT NULL,
    `description` varchar(255) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


--
-- Dumping data for table `permission`
--

LOCK TABLES `permission` WRITE;
/*!40000 ALTER TABLE `permission`
    DISABLE KEYS */;
INSERT INTO `permission`
VALUES (1, 'read', '读取'),
       (2, 'write', '写入');
/*!40000 ALTER TABLE `permission`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client = @@character_set_client */;

CREATE TABLE `role`
(
    `role_id`          bigint(20)   NOT NULL,
    `role_name`        varchar(255) NOT NULL,
    `role_description` varchar(255) NOT NULL,
    `create_time`      datetime     not null default NOW() comment '创建时间',
    `update_time`      datetime     not null default NOW() comment '创建时间',
    `search_value`     varchar(20)  not null default '' comment '查询参数',
    `create_by`        varchar(255) not null default '' comment '创建人',
    `update_by`        varchar(255) not null default '' comment '最后更新人',
    `remark`           varchar(255) not null default '' comment '创建时间',
    PRIMARY KEY (`role_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role`
    DISABLE KEYS */;
INSERT INTO `role`
VALUES (1, 'admin', '管理团', now(), now(), 'stan', '', '', ''),
       (2, 'user', '普通用户', now(), now(), 'stan', '', '', '');
/*!40000 ALTER TABLE `role`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role_permission`
--

DROP TABLE IF EXISTS `role_permission`;
/*!40101 SET @saved_cs_client = @@character_set_client */;

CREATE TABLE `role_permission`
(
    `id`            bigint(20) NOT NULL,
    `role_id`       bigint(20) NOT NULL,
    `permission_id` bigint(20) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


--
-- Dumping data for table `role_permission`
--

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

--
-- Table structure for table `school_info`
--

DROP TABLE IF EXISTS `school_info`;
/*!40101 SET @saved_cs_client = @@character_set_client */;

CREATE TABLE `school_info`
(
    `school_id`    bigint(20)   NOT NULL,
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


--
-- Dumping data for table `school_info`
--

LOCK TABLES `school_info` WRITE;
/*!40000 ALTER TABLE `school_info`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `school_info`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_listen_port`
--

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

--
-- Table structure for table `t_sign_record`
--

DROP TABLE IF EXISTS `t_sign_record`;
/*!40101 SET @saved_cs_client = @@character_set_client */;

CREATE TABLE `t_sign_record`
(
    `id`                  bigint(20)   NOT NULL AUTO_INCREMENT COMMENT '自增键',
    `user_id`             bigint(20)   NOT NULL COMMENT '索引，用户表的id',
    `date_month`          date         NOT NULL COMMENT '索引，月份，形如2019-02',
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

--
-- Table structure for table `upload_file`
--

DROP TABLE IF EXISTS `upload_file`;
/*!40101 SET @saved_cs_client = @@character_set_client */;

CREATE TABLE `upload_file`
(
    `file_id`      bigint(20)   NOT NULL AUTO_INCREMENT,
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

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client = @@character_set_client */;

CREATE TABLE `user`
(
    `userid`       bigint(20)    NOT NULL AUTO_INCREMENT,
    `username`     varchar(10)            DEFAULT NULL COMMENT '用户登录的账号，长度为十位字符',
    `nickname`     nvarchar(20)  NOT NULL UNIQUE COMMENT '用户界面上展示的昵称',
    `avatar`       varchar(255)  NOT NULL DEFAULT '' COMMENT '用户的头像地址',
    `description`  nvarchar(200) NOT NULL DEFAULT '' COMMENT '个人简介',
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


INSERT INTO `user`
VALUES (123, 'stan', '纯纯的黑色幽默', 'avatar_path', 'description', '1576302867@qq.com',
        '13628091432', '$2a$10$45ScSS1BeuYizV2QYJ9HVOfpBoTOxnjXyCkNPFkTJnf9o3bW0l.4G',
        '13628091432', now(), now(), 'stan', '', '', '');

DROP TABLE IF EXISTS `user_role`;
/*!40101 SET @saved_cs_client = @@character_set_client */;

CREATE TABLE `user_role`
(
    `id`           bigint(20)   NOT NULL,
    `role_id`      bigint(20)   NOT NULL,
    `user_id`      bigint(20)   NOT NULL,
    `create_time`  datetime     not null default NOW() comment '创建时间',
    `update_time`  datetime     not null default NOW() comment '创建时间',
    `search_value` varchar(20)  not null default '' comment '查询参数',
    `create_by`    varchar(255) not null default '' comment '创建人',
    `update_by`    varchar(255) not null default '' comment '最后更新人',
    `remark`       varchar(255) not null default '' comment '创建时间'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

--
-- Table structure for table `wechat_content`
--

DROP TABLE IF EXISTS `wechat_content`;
/*!40101 SET @saved_cs_client = @@character_set_client */;

CREATE TABLE `wechat_content`
(
    `id`               bigint(20)   NOT NULL AUTO_INCREMENT,
    `x_create_userid`  bigint(20)   NOT NULL,
    `x_last_edit_time` bigint(20)   NOT NULL,
    `x_create_time`    bigint(20)   NOT NULL,
    `x_data_flag`      bit(1)       NOT NULL,
    `content`          varchar(255)          DEFAULT NULL,
    `head_img_url`     varchar(255)          DEFAULT NULL,
    `message_type`     int(11)               DEFAULT NULL,
    `user_id`          bigint(20)   NOT NULL,
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


--
-- Dumping data for table `wechat_content`
--

LOCK TABLES `wechat_content` WRITE;
/*!40000 ALTER TABLE `wechat_content`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `wechat_content`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wechat_content_wechat_images`
--

DROP TABLE IF EXISTS `wechat_content_wechat_images`;
/*!40101 SET @saved_cs_client = @@character_set_client */;

CREATE TABLE `wechat_content_wechat_images`
(
    `wechat_content_id` bigint(20)   NOT NULL,
    `wechat_images_id`  bigint(20)   NOT NULL,
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


--
-- Dumping data for table `wechat_content_wechat_images`
--

LOCK TABLES `wechat_content_wechat_images` WRITE;
/*!40000 ALTER TABLE `wechat_content_wechat_images`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `wechat_content_wechat_images`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wechat_images`
--

DROP TABLE IF EXISTS `wechat_images`;
/*!40101 SET @saved_cs_client = @@character_set_client */;

CREATE TABLE `wechat_images`
(
    `id`                bigint(20)   NOT NULL AUTO_INCREMENT,
    `x_create_userid`   bigint(20)   NOT NULL,
    `x_last_edit_time`  bigint(20)   NOT NULL,
    `x_create_time`     bigint(20)   NOT NULL,
    `x_data_flag`       bit(1)       NOT NULL,
    `image_url`         varchar(255)          DEFAULT NULL,
    `image_name`        varchar(255)          DEFAULT NULL,
    `wechat_content_id` bigint(20)            DEFAULT NULL,
    `create_time`       datetime     not null default NOW() comment '创建时间',
    `update_time`       datetime     not null default NOW() comment '创建时间',
    `search_value`      varchar(20)  not null default '' comment '查询参数',
    `create_by`         varchar(255) not null default '' comment '创建人',
    `update_by`         varchar(255) not null default '' comment '最后更新人',
    `remark`            varchar(255) not null default '' comment '创建时间',
    PRIMARY KEY (`id`),
    KEY `content_id_index` (`wechat_content_id`)
) ENGINE = MyISAM
  DEFAULT CHARSET = utf8mb4;


--
-- Dumping data for table `wechat_images`
--

LOCK TABLES `wechat_images` WRITE;
/*!40000 ALTER TABLE `wechat_images`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `wechat_images`
    ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE = @OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE = @OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT = @OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS = @OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION = @OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES = @OLD_SQL_NOTES */;

-- Dump completed on 2021-01-26  0:46:52
