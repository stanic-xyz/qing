#### 安装PostgreSql服务

```bash

sudo apt update
sudo apt install postgresql postgresql-contrib

sudo systemctl start postgresql.service

#查看服务的状态

sudo systemctl status postgresql

```

如果服务正在运行。你将看到“active (running)”的状态信息。

PostgreSQL安装完成后，会自动创建一个默认的数据库集群和一个名为“postgres”的超级用户。默认情况下，这个用户的密码是空的。为了安全起见，你应该立即修改postgres用户的密码。可以使用以下命令来修改密码：

```bash
sudo passwd postgres
```

#### 创建数据库用户

```bash

# 切换到用户postgres并打开一个Pg命令行界面

sudo -u postgres psql

postgres=# CREATE

ROLE your_user_name WITH LOGIN PASSWORD 'your_password';
```

#### 开启PostgreSql的远程访问

要开启PostgreSQL的远程访问，你需要进行以下步骤来配置数据库服务器的访问权限和监听地址：

##### 定位配置文件：

使用以下命令切换到postgres超级用户：

```bash
sudo -i -u postgres
```

输入psql进入PostgreSQL数据库提供的命令行终端程序。
输入SHOW config_file;查看当前服务器的配置文件路径。
输入\q退出psql。

##### 修改配置文件：

###### 修改postgresql.conf配置文件：

找到#listen_addresses = 'localhost'这一行，将其修改为listen_addresses = '*'。这里的*表示允许数据库服务器监听来自任何主机的连接请求。
修改pg_hba.conf配置文件：
配置对数据库的访问权限。你可以添加或修改行来允许特定IP地址或范围的IP地址访问。例如，添加以下行来允许所有IP地址使用md5加密方式访问数据库：
host all all 0.0.0.0/0 md5
如果你只想允许特定IP地址或子网访问，你可以相应地修改CIDR表示法。

##### 重启PostgreSQL服务：

为了让配置更改生效，你需要重启PostgreSQL服务。使用以下命令重启服务：

```bash

sudo service postgresql restart
#sudo systemctl restart postgresql

```

防火墙设置：
确保你的服务器防火墙允许PostgreSQL的默认端口（通常是5432）的入站连接。你可能需要配置ufw或其他防火墙工具来打开这个端口。

网络配置：
如果你的PostgreSQL服务器和客户端位于不同的网络环境中（例如，不同的子网或使用了NAT），你可能还需要配置路由器或防火墙以允许通过特定端口（默认为5432）的流量。

测试连接：
从远程计算机上，使用适当的客户端工具（如psql命令行工具或其他数据库管理工具）尝试连接到PostgreSQL服务器。确保提供正确的用户名、密码和数据库名称。

请注意，开启远程访问可能会增加安全风险。确保你使用强密码，并定期更新和审查访问权限。此外，考虑使用SSL来加密客户端和服务器之间的通信，以提供额外的安全性。
