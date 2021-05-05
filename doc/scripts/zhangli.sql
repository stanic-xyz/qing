-- MySQL dump 10.13  Distrib 8.0.17, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: zhangli
-- ------------------------------------------------------
-- Server version	8.0.17

/*!40101 SET @OLD_CHARACTER_SET_CLIENT = @@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS = @@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION = @@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE = @@TIME_ZONE */;
/*!40103 SET TIME_ZONE = '+00:00' */;
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
    PRIMARY KEY (`activity_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8;


--
-- Dumping data for table `activity`
--

LOCK TABLES `activity` WRITE;
/*!40000 ALTER TABLE `activity`
    DISABLE KEYS */;
INSERT INTO `activity`
VALUES (1, '123123');
/*!40000 ALTER TABLE `activity`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `anime_conment`
--

DROP TABLE IF EXISTS `anime_conment`;
/*!40101 SET @saved_cs_client = @@character_set_client */;

CREATE TABLE `anime_conment`
(
    `id`       bigint(20) NOT NULL AUTO_INCREMENT COMMENT '评论ID',
    `sid`      bigint(20)   DEFAULT NULL COMMENT 'id',
    `username` varchar(255) DEFAULT NULL COMMENT '评论者昵称',
    `content`  text       NOT NULL COMMENT '评论内容',
    `cid`      bigint(20) NOT NULL COMMENT '番剧ID',
    `time`     bigint(20)   DEFAULT NULL COMMENT '时间戳',
    `ip`       varchar(15)  DEFAULT NULL COMMENT 'ip地址',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

--
-- Dumping data for table `anime_conment`
--

LOCK TABLES `anime_conment` WRITE;
/*!40000 ALTER TABLE `anime_conment`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `anime_conment`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `anime_district`
--

DROP TABLE IF EXISTS `anime_district`;
/*!40101 SET @saved_cs_client = @@character_set_client */;

CREATE TABLE `anime_district`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name`        varchar(50)                                             DEFAULT NULL COMMENT '地区名称',
    `code`        varchar(10)                                             DEFAULT NULL COMMENT '地区编码',
    `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '描述信息',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 4
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;


--
-- Dumping data for table `anime_district`
--

LOCK TABLES `anime_district` WRITE;
/*!40000 ALTER TABLE `anime_district`
    DISABLE KEYS */;
INSERT INTO `anime_district`
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
    `id`             bigint(20)   NOT NULL AUTO_INCREMENT COMMENT '视频ID',
    `anime_id`       bigint(20)   DEFAULT NULL COMMENT '动漫ID',
    `name`           varchar(255) NOT NULL COMMENT '视频标题名称',
    `status`         int(11)      DEFAULT '0' COMMENT '视频状态，0正常',
    `uploadder_name` varchar(100) DEFAULT NULL COMMENT '上传者名称',
    `uploader_id`    bigint(20)   DEFAULT NULL COMMENT '上传用户ID',
    `upload_time`    datetime     DEFAULT NULL COMMENT '视频上传时间',
    `url1`           varchar(255) NOT NULL COMMENT '视频地址',
    `url3`           varchar(255) DEFAULT NULL COMMENT '视频播放地址3',
    `url2`           varchar(255) DEFAULT NULL COMMENT '视频播放地址2',
    `order_no`       int(11)      NOT NULL COMMENT '视频排序',
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
    `id`                bigint(20) NOT NULL AUTO_INCREMENT,
    `type`              int(11)    DEFAULT NULL COMMENT '反馈类型\n1、链接失效\n2、资源质量差\n3、集数缺失\n4、其他',
    `detail`            text COMMENT '详细信息',
    `mid`               bigint(20) NOT NULL COMMENT '番剧ID',
    `create_time`       datetime   NOT NULL COMMENT '反馈时间',
    `uid`               bigint(20) DEFAULT NULL COMMENT '用户ID',
    `processing_status` int(11)    NOT NULL COMMENT '处理状态\n0、未处理\n1、处理中\n2、已处理',
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
    `district`         varchar(255) DEFAULT NULL COMMENT '地区(日本、中国、欧美）',
    `cover_url`        varchar(255) NOT NULL COMMENT '封面地址',
    `type`             varchar(255) DEFAULT NULL COMMENT 'OVA\nTV\n剧场版(theater_version)',
    `instruction`      text COMMENT '番剧介绍',
    `other_name`       varchar(255) DEFAULT NULL COMMENT '其它名称',
    `author`           varchar(255) DEFAULT NULL COMMENT '原作',
    `premiere_date`    varchar(255) DEFAULT NULL COMMENT '首播时间',
    `company`          varchar(255) DEFAULT NULL COMMENT '制作公司',
    `play_status`      varchar(255) DEFAULT '0' COMMENT '0 未播放\n1 连载\n2 完结',
    `plot_type`        varchar(255) DEFAULT NULL COMMENT '剧情类型',
    `tags`             varchar(255) DEFAULT NULL COMMENT '标签',
    `official_website` varchar(255) DEFAULT NULL COMMENT '官方网站',
    `play_heat`        varchar(255) DEFAULT NULL COMMENT '播放热度',
    `orignal_name`     varchar(255) DEFAULT NULL COMMENT '原版名称',
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
    `id`   bigint(20)   DEFAULT NULL COMMENT 'id',
    `name` varchar(30)  DEFAULT NULL COMMENT '名称',
    `path` varchar(100) DEFAULT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


--
-- Dumping data for table `anime_menu`
--

LOCK TABLES `anime_menu` WRITE;
/*!40000 ALTER TABLE `anime_menu`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `anime_menu`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `anime_resource`
--

DROP TABLE IF EXISTS `anime_resource`;
/*!40101 SET @saved_cs_client = @@character_set_client */;

CREATE TABLE `anime_resource`
(
    `id`          bigint(20)   NOT NULL AUTO_INCREMENT,
    `mid`         bigint(20)   NOT NULL COMMENT '番剧ID',
    `name`        varchar(255) NOT NULL COMMENT '播放资源列表名称',
    `create_time` datetime     NOT NULL,
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
    `id`          bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name`        varchar(10) NOT NULL COMMENT '类型名称',
    `description` text COMMENT '详细介绍信息',
    `order`       int(11) DEFAULT NULL COMMENT '排序号',
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
INSERT INTO `anime_type`
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
    `vid`         bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `code`        varchar(10) NOT NULL,
    `name`        varchar(50) NOT NULL COMMENT '名称',
    `description` varchar(255) DEFAULT NULL COMMENT '描述信息',
    PRIMARY KEY (`vid`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 8
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;


--
-- Dumping data for table `anime_version`
--

LOCK TABLES `anime_version` WRITE;
/*!40000 ALTER TABLE `anime_version`
    DISABLE KEYS */;
INSERT INTO `anime_version`
VALUES (1, 'TV', 'TV版本', '描述信息'),
       (2, 'D', '剧场版', '描述新城'),
       (3, 'OVA', 'OVA', '描述信息'),
       (4, 'WEB', '网络剧', '网络剧'),
       (5, 'ST', '短片同人', '短片同人'),
       (6, 'DA', '动态漫画', '动态漫画'),
       (7, 'MOVIE', '电影', '电影');
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
    `id`          bigint(20) NOT NULL AUTO_INCREMENT,
    `car_img`     varchar(255) DEFAULT NULL,
    `car_number`  varchar(255) DEFAULT NULL,
    `car_status`  int(11)      DEFAULT NULL,
    `user_userid` bigint(20)   DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FKpfqcaiuly21miefv7d0yjag0f` (`user_userid`)
) ENGINE = MyISAM
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;


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
    `id`          bigint(20) NOT NULL AUTO_INCREMENT,
    `course_name` varchar(255) DEFAULT NULL,
    `course_time` date         DEFAULT NULL,
    `create_time` date         DEFAULT NULL,
    `car_id`      bigint(20)   DEFAULT NULL,
    `user_userid` bigint(20)   DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FKr10oqjrd9sp3e6e5f80a5ahca` (`car_id`),
    KEY `FKo89po0h3p5kaem9q5dhidm7ey` (`user_userid`)
) ENGINE = MyISAM
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;


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
VALUES (1, 'admin', '管理团'),
       (2, 'user', '普通用户');
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
    `school_id`   bigint(20) NOT NULL,
    `school_code` varchar(255) DEFAULT NULL,
    `school_name` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`school_id`)
) ENGINE = MyISAM
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;


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
    `listen_port`   int(11)      DEFAULT NULL,
    `port_describe` varchar(255) DEFAULT NULL,
    `dest_ip`       varchar(255) DEFAULT NULL,
    `dest_port`     int(11)      DEFAULT NULL,
    `on_start`      tinyint(1)   DEFAULT NULL,
    `port_type`     int(11)      DEFAULT NULL,
    `cert_path`     varchar(255) DEFAULT NULL,
    `cert_password` varchar(255) DEFAULT NULL,
    `gmt_create`    datetime     DEFAULT NULL,
    `gmt_modify`    datetime     DEFAULT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


--
-- Dumping data for table `t_listen_port`
--

LOCK TABLES `t_listen_port` WRITE;
/*!40000 ALTER TABLE `t_listen_port`
    DISABLE KEYS */;
INSERT INTO `t_listen_port`
VALUES (8880, 'string', '127.0.0.1', 8080, 1, 0, NULL, NULL, NULL, NULL);
/*!40000 ALTER TABLE `t_listen_port`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_sign_record`
--

DROP TABLE IF EXISTS `t_sign_record`;
/*!40101 SET @saved_cs_client = @@character_set_client */;

CREATE TABLE `t_sign_record`
(
    `id`                  bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增键',
    `user_id`             bigint(20) NOT NULL COMMENT '索引，用户表的id',
    `date_month`          date       NOT NULL COMMENT '索引，月份，形如2019-02',
    `mask`                int(32) DEFAULT NULL COMMENT '用户签到的数据',
    `continue_sign_month` int(11) DEFAULT '0' COMMENT '连续签到天数',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8;


--
-- Dumping data for table `t_sign_record`
--

LOCK TABLES `t_sign_record` WRITE;
/*!40000 ALTER TABLE `t_sign_record`
    DISABLE KEYS */;
INSERT INTO `t_sign_record`
VALUES (1, 2, '2019-05-20', 50, 5);
/*!40000 ALTER TABLE `t_sign_record`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `upload_file`
--

DROP TABLE IF EXISTS `upload_file`;
/*!40101 SET @saved_cs_client = @@character_set_client */;

CREATE TABLE `upload_file`
(
    `fileid`    bigint(20) NOT NULL AUTO_INCREMENT,
    `file_name` varchar(255) DEFAULT NULL,
    `file_size` bigint(20)   DEFAULT NULL,
    `mime_type` varchar(255) DEFAULT NULL,
    `url`       varchar(255) DEFAULT NULL,
    PRIMARY KEY (`fileid`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 138
  DEFAULT CHARSET = utf8;


--
-- Dumping data for table `upload_file`
--

LOCK TABLES `upload_file` WRITE;
/*!40000 ALTER TABLE `upload_file`
    DISABLE KEYS */;
INSERT INTO `upload_file`
VALUES (1, '霸业淮竹.jpg', 228550, 'image/jpeg',
        'http://www.stanic.xyz/pic/2020-03-2769bab9cc-59d3-463d-b7c5-afaf9749be3e.jpg'),
       (2, '霸业淮竹.jpg', 228550, 'image/jpeg',
        'http://www.stanic.xyz/pic/2020-03-27250402d1-c20f-4aa5-9264-eb5c2a81344e.jpg'),
       (3, '6265139e53bb7a5f7ab9cc8783d4b4a7.jpg', 29250, 'image/jpeg',
        'http://www.stanic.xyz/pic/2020-03-273d22cb8e-96fe-43f3-b394-34c24789a54d.jpg'),
       (123, '123', 123123, '123123', 'http://www.baidu.com'),
       (124, '可爱头像1.jpg', 71000, 'image/jpeg',
        'http://www.stanic.xyz/pic/2020-03-2722b77470-d432-4971-922a-0629c23a3358.jpg'),
       (125, '可爱头像1.jpg', 71000, 'image/jpeg',
        'http://www.stanic.xyz/pic/2020-03-2769d2a814-4efa-4485-b16b-029d2a6d7856.jpg'),
       (126, '可爱头像1.jpg', 71000, 'image/jpeg',
        'http://www.stanic.xyz/pic/2020-03-27b1586489-1481-4536-bbdf-8b584cbd1082.jpg'),
       (127, 'voletile原理.png', 68648, 'image/png',
        'http://www.stanic.xyz/pic/2020-06-06b4bb56c2-72cc-4bd0-b11d-656f92461519.png'),
       (128, 'keyframe03161751000010346218iuj9fp.jpg', 35325, 'image/jpeg',
        'http://www.stanic.xyz/pic/2020-06-06747920f8-8cd7-4b7e-8379-eca2f0b80e85.jpg'),
       (129, '653ebe8db712d8c98e244fe2b8913740f6dd6e22.jpg@206w_116h_1c_100q.webp', 17446, 'image/webp',
        'http://www.stanic.xyz/pic/2020-06-06a3bb705e-23ad-474e-8a3a-0b52469ea4e5.webp'),
       (130, '头像地址.png', 46028, 'image/png',
        'http://www.stanic.xyz/pic/2020-06-066a60f03a-6fc4-4ce2-acf1-2b7659d0732b.png'),
       (131, '头像地址.png', 46028, 'image/png',
        'http://www.stanic.xyz/pic/2020-06-062928ead8-5fa1-476a-a389-3535815b2383.png'),
       (132, 'keyframe03161751000010346218iuj9fp.jpg', 35325, 'image/jpeg',
        'http://www.stanic.xyz/pic/2020-06-06672c4a8e-7890-4915-985b-55bec35c75c4.jpg'),
       (133, 'keyframe03161751000010346218iuj9fp.jpg', 35325, 'image/jpeg',
        'http://www.stanic.xyz/pic/2020-06-07349cbe33-8c5b-4eaf-ac1a-2b5733c44140.jpg'),
       (134, '江厌离.png', 817260, 'image/png',
        'http://www.stanic.xyz/pic/2020-06-233001aea4-d109-467c-a823-6debe44f3ffb.png'),
       (135, 'output.json', 371, 'application/json',
        'http://www.stanic.xyz/pic/2020-06-2371dc8ab7-8f2d-46ef-b756-470ea233fd74.json'),
       (136, 'RO姬2.webp', 1363428, 'image/webp',
        'http://www.stanic.xyz/pic/2020-11-04b2ca96b9-fd3c-409d-9b3d-8e0557aac5a1.webp'),
       (137, '头像地址.png', 46028, 'image/png',
        'http://www.stanic.xyz/pic/2021-01-09c250576e-e60b-469a-931e-596d60cfa2aa.png');
/*!40000 ALTER TABLE `upload_file`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client = @@character_set_client */;

CREATE TABLE `user`
(
    `userid`      bigint(20)    NOT NULL AUTO_INCREMENT,
    `username`    varchar(10)                   DEFAULT NULL COMMENT '用户登录的账号，长度为十位字符',
    `nickname`    nvarchar(20)  NOT NULL UNIQUE DEFAULT NULL COMMENT '用户界面上展示的昵称',
    `avatar`      varchar(255)  NOT NULL        DEFAULT '' COMMENT '用户的头像地址',
    `description` nvarchar(200) NOT NULL        DEFAULT '' COMMENT '个人简介',
    `email`       varchar(255)                  DEFAULT NULL,
    `open_id`     varchar(30)                   DEFAULT NULL,
    `password`    varchar(255)                  DEFAULT NULL,
    `phone`       varchar(255)                  DEFAULT NULL,
    PRIMARY KEY (`userid`),
    UNIQUE KEY `UK_sb8bbouer5wak8vyiiy4pf2bx` (`username`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 124
  DEFAULT CHARSET = utf8;


--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user`
    DISABLE KEYS */;
INSERT INTO `user`
VALUES (123, '1576302867@qq.com', '12312312', '$2a$10$45ScSS1BeuYizV2QYJ9HVOfpBoTOxnjXyCkNPFkTJnf9o3bW0l.4G',
        '13628091432', 'stan');
/*!40000 ALTER TABLE `user`
    ENABLE KEYS */;
UNLOCK TABLES;


--
-- Table structure for table `user_role`
--

DROP TABLE IF EXISTS `user_role`;
/*!40101 SET @saved_cs_client = @@character_set_client */;

CREATE TABLE `user_role`
(
    `id`      bigint(20) NOT NULL,
    `role_id` bigint(20) NOT NULL,
    `user_id` bigint(20) NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


--
-- Dumping data for table `user_role`
--

LOCK TABLES `user_role` WRITE;
/*!40000 ALTER TABLE `user_role`
    DISABLE KEYS */;
INSERT INTO `user_role`
VALUES (1, 1, 123);
/*!40000 ALTER TABLE `user_role`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wechat_content`
--

DROP TABLE IF EXISTS `wechat_content`;
/*!40101 SET @saved_cs_client = @@character_set_client */;

CREATE TABLE `wechat_content`
(
    `id`               bigint(20) NOT NULL AUTO_INCREMENT,
    `x_create_userid`  bigint(20) NOT NULL,
    `x_last_edit_time` bigint(20) NOT NULL,
    `x_create_time`    bigint(20) NOT NULL,
    `x_data_flag`      bit(1)     NOT NULL,
    `content`          varchar(255) DEFAULT NULL,
    `head_img_url`     varchar(255) DEFAULT NULL,
    `message_type`     int(11)      DEFAULT NULL,
    `user_id`          bigint(20) NOT NULL,
    `username`         varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = MyISAM
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;


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
    `wechat_content_id` bigint(20) NOT NULL,
    `wechat_images_id`  bigint(20) NOT NULL,
    UNIQUE KEY `UK_gnpdvk1n2a4eu10wiitxr5lr6` (`wechat_images_id`),
    KEY `FKn11v52h3feurudah9x6cl1jh7` (`wechat_content_id`)
) ENGINE = MyISAM
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;


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
    `id`                bigint(20) NOT NULL AUTO_INCREMENT,
    `x_create_userid`   bigint(20) NOT NULL,
    `x_last_edit_time`  bigint(20) NOT NULL,
    `x_create_time`     bigint(20) NOT NULL,
    `x_data_flag`       bit(1)     NOT NULL,
    `image_url`         varchar(255) DEFAULT NULL,
    `image_name`        varchar(255) DEFAULT NULL,
    `wechat_content_id` bigint(20)   DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FKi100wdu51bbo65gc5ioxryw7i` (`wechat_content_id`)
) ENGINE = MyISAM
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;


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
