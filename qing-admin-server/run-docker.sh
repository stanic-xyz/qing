#
# Copyright (c) 2019-2023  YunLong Chen
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

#!/bin/bash

docker run -it -d --name=admin-server -p10086:80/tcp \
-e QING_ADMIN_IP=121.36.163.95 \
-e QING_ADMIN_HOST=admin.chenyunlong.cn \
-e QING_ADMIN_PORT=10086 \
-e QING_ADMIN_PROTOCOL=https \
-e QING_REGISTRY_EUREKA_URL=http://121.36.163.95:8761/eureka/ \
--hostname=admin.chenyunlong.cn qing-admin-server
