#### 下载 Docker 安装包

在达梦数据库官网下载 [Docker 安装包](https://eco.dameng.com/download/)。

#### 导入安装包

拷贝安装包到 /opt 目录下（或其他任意目录），执行以下命令导入安装包：

```shell
docker load -i /opt/dm8_20220822_rev166351_x86_rh6_64_ctm.tar
```

安装服务

### Docker运行

```shell
docker run -d -p 5236:5236 --restart=always --name dm8_01 --privileged=true -e PAGE_SIZE=16 -e LD_LIBRARY_PATH=/opt/dmdbms/bin -e INSTANCE_NAME=dm8_01 -v /data/dm8_01:/opt/dmdbms/data dm8_single:v8.1.2.128_ent_x86_64_ctm_pack4
```

启动dm8_test为启动的容器名称，启动完成后，可通过日志检查启动情况，命令如下：

```shell
docker logs -f dm8_test
```

#### 启动/停止数据库

停止数据库命令如下：

```shell
# 停止
docker stop dm8_test

# 启动
docker start dm8_test

# 重启
docker restart dm8_test
```


