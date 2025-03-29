#!/usr/bin/env bash

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

for file in $(find ./ -name "*.sql" -exec ls {} \; | grep -v postgres | sort | tr ' ' '|' | tr '\n' ' '); do
  file=$(echo ${file} | tr '|' ' ')
  printf "Applying update ${file}\n"
  mysql -uroot -p$MYSQL_ROOT_PASSWORD -h mysql <${file}
done
