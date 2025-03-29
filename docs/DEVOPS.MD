### 构建脚本

```shell
mvn -B clean package -Dmaven.test.skip=true -Dautoconfig.skip -Ppro -U
```

### 构建物上传

target/op-system-center.tar.gz

目标机器的目录(有权限的目录)
/home/admin/system/package.tgz

### 部署脚本

```shell
mkdir -p /home/admin/system_${BUILD_NUMBER}
tar zxvf /home/admin/system/package.tgz -C /home/admin/system_${BUILD_NUMBER}
cd /home/admin/system_${BUILD_NUMBER}
tar -zxvf op-system-center.tar.gz
sh /home/admin/system_${BUILD_NUMBER}/op-system-center/bin/start.sh start
```

### 环境变量

``` text
[21:52:25] [INFO] oss-cn-beijing-internal.aliyuncs.com
[21:52:25] [INFO]PIPELINE_ID=1929979
[21:52:25] [INFO]PIPELINE_NAME=流水线 2022-08-30
[21:52:25] [INFO]BUILD_NUMBER=5
[21:52:25] [INFO]FILE_PARENT_ID=aone2/2045063/1661867544950
[21:52:25] [INFO]FILE_ID=aone2/2045063/1661867544950/Artifacts_1929979.tgz
[21:52:25] [INFO]EMPLOYEE_ID=5f9aba67a3bcee4a483449b5
[21:52:25] [INFO]WORK_SPACE=/root/workspace
[21:52:25] [INFO]PROJECT_DIR=/root/workspace/op-system-center_FnAv
[21:52:25] [INFO]BUILD_JOB_ID=79311264
[21:52:25] [INFO]GIT_IGNORE=
[21:52:25] [INFO]uploadType=PUBLIC_STORAGE
[21:52:25] [INFO]SERVICE_CONNECTION_ID=
[21:52:25] [INFO]ARTIFACT=Artifacts_1929979
[21:52:25] [INFO]ARTIFACT_VERSION=2022-08-30-21-50-51
[21:52:25] [INFO]REPO_ID=
[21:52:25] [INFO]ORG_ID=5f9aba853a5188f27f3f43ba
[21:52:25] [INFO]REGION_ID=
[21:52:25] [INFO]INCLUDE_PATH_IN_ARTIFACT=false
[21:52:25] [INFO]ARCHIVE_PATH=target/op-system-center.tar.gz

```

### shell 脚本解释

https://www.shell.how/

https://explainshell.com/

### 前端脚本

```shell
cnpm install
npm run build:prod
```

构建物上传 dist

### 下载目录

/tmp/qing-ui.tgz

```shell
tar zxvf /tmp/qing-ui.tgz -C /home/admin/qing-ui/dist
```

### nginx 配置

```nginx configuration
server {
    listen 80;
    server_name myadmin.example.com;
    location / {
        root /home/admin/only4play-ui/dist;
        index index.html index.htm;
        try_files $uri $uri/ /index.html;
    }
    location ^~ /api/ {
        proxy_set_header Host $http_host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        proxy_pass http://localhost:8889/;
        proxy_max_temp_file_size 2m;
        proxy_connect_timeout 90;
        proxy_send_timeout 90;
        proxy_read_timeout 90;
        proxy_buffer_size 4k;
        proxy_buffers 4 32k;
        proxy_busy_buffers_size 64k;
        proxy_temp_file_write_size 64k;
        client_max_body_size 5m;
    }

    access_log  /www/wwwlogs/gateway.shutu516.com.log;
    error_log  /www/wwwlogs/gateway.shutu516.com.error.log;
}
```
