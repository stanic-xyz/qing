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


alter table anime_episode
    add playlist_id bigint not null comment '播放列表ID' after anime_id;

create unique index user_email_index on qingUser (email);

create unique index user_username_index on qingUser (username);


SELECT concat('ALTER TABLE ', table_schema, '.', table_name,
              ' add column create_time  int, add column update_time  datetime,add column search_value varchar(255),add column create_by    varchar(255), add column update_by    varchar(255),add column remark      varchar(255);')
FROM information_schema.TABLES t
WHERE table_schema = 'qing';

ALTER TABLE qing.anime_info
    add column create_time int,
    add column update_time  datetime,
    add column search_value varchar(255),
    add column create_by    varchar(255),
    add column update_by    varchar(255),
    add column remark       varchar(255);

alter table activity
    add column createTime int,
    add column updateTime  datetime,
    add column searchValue varchar(255),
    add column createBy    varchar(255),
    add column updateBy    varchar(255),
    add column remark      varchar(255);


DROP PROCEDURE IF EXISTS addColumn;
DELIMITER
$$

# 为所有的表添加公共列
CREATE PROCEDURE addColumn()
BEGIN
    -- 定义表名变量
    DECLARE
s_tablename VARCHAR(100);

    /*显示表的数据库中的所有表
    SELECT table_name FROM information_schema.tables WHERE table_schema='databasename' Order by table_name ;
    */

#显示所有
    DECLARE
cur_table_structure CURSOR
        FOR
SELECT table_name
FROM INFORMATION_SCHEMA.TABLES
-- test = 数据库名称
WHERE table_schema = 'qing'
  AND table_name NOT IN (SELECT t.table_name
                         FROM (SELECT table_name, column_name
                               FROM information_schema.columns
                               WHERE table_name IN (SELECT table_name
                                                    FROM INFORMATION_SCHEMA.TABLES
                                                    WHERE table_schema = 'qing')) t
                         WHERE t.column_name = 'object_name');

DECLARE
CONTINUE HANDLER FOR SQLSTATE '02000' SET s_tablename = NULL;

OPEN cur_table_structure;

FETCH cur_table_structure INTO s_tablename;

WHILE
(s_tablename IS NOT NULL)
        DO
            SET @MyQuery = CONCAT('alter table `', s_tablename,
                                  '` add column create_time  int, add column update_time  datetime,add column search_value varchar(255),add column create_by    varchar(255), add column update_by    varchar(255),add column remark      varchar(255)');
PREPARE msql FROM @MyQuery;

EXECUTE msql;
#USING
@c;

FETCH cur_table_structure INTO s_tablename;
END WHILE;
CLOSE cur_table_structure;


END;
$$

CALL addColumn();

