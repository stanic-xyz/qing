#
# Copyright (c) 2019-2022 YunLong Chen
# Project Qing is licensed under Mulan PSL v2.
# You can use this software according to the terms and conditions of the Mulan PSL v2.
# You may obtain a copy of Mulan PSL v2 at:
#          http://license.coscl.org.cn/MulanPSL2
# THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
# EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
# MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
# See the Mulan PSL v2 for more details.
#
#

docker run -it -d -e QING_EUREKA_HOST=eureka.chenyunlong.cn \
 -e QING_REGISTRY_EUREKA_PORT=80 \
 -e QING_MYSQL_HOST=sh-cynosdbmysql-grp-e1mnik7m.sql.tencentcdb.com \
 -e QING_MYSQL_PORT=28661 \
 -e QING_MYSQL_USERNAME=root \
 -e QING_MYSQL_PASSWORD=MysqlPwdROOT1996 \
 -e QING_MAIL_HOST=smtp.qq.com \
 -e QING_MAIL_USERNAME=1576302867@qq.com \
 -e QING_MAIL_PASSWORD=xyshwmdggdtqfjhb \
 -e QING_REDIS_HOST=localhost \
 -e QING_SERVICE_ANIME_HOST=bangumi.chenyunlong.cn \
 -e QING_REDIS_PORT=6379 \
 -p8080:80 --name anime-service {registry}/qing-service-anime:1.0.0.RELEASE
