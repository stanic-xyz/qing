create table anime_playlist
(
    id          bigint auto_increment comment '播放列表主键ID',
    name        nvarchar(255) not null comment '播放列表名称',
    anime_id    bigint        not null comment '所属动漫主键ID',
    description text          null comment '描述信息',
    constraint table_playlist_pk
        primary key (id)
);

alter table anime_episode
    add playlist_id bigint not null comment '播放列表ID' after anime_id;