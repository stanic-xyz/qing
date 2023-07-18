在项目里面与文件存储相关的，统一使用OSS服务

这里选用的服务是MinIO这个服务，在Windows上的安装步骤如下：

```
1. 下载MinIO客户端，可以使用MinIO的官方客户端，也可以使用MinIO的社区版本
2. 安装MinIO客户端
3. 启动MinIO服务
```

在linux上的安装步骤如下：

``` bash
$ wget https://dl.min.io/server/minio/release/linux-amd64/minio
$ chmod +x minio
$ sudo mv minio /usr/local/bin/minio
$ sudo mkdir -p /data/minio
$ sudo chown -R minio:minio /data/minio
$ sudo ./minio server /data/minio
```
