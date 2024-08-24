CREATE DATABASE kuboard DEFAULT CHARACTER SET = 'utf8mb4' DEFAULT COLLATE = 'utf8mb4_unicode_ci';
create user 'kuboard'@'%' identified by 'kuboardpwd';
grant all privileges on kuboard.* to 'kuboard'@'%';
FLUSH PRIVILEGES;
