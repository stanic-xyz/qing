```markdown
# 飞牛OS家庭服务器备份与恢复方案  
`适用环境：飞牛OS + 1Panel容器管理`  

## 📁 备份策略配置
### 存储路径规划
```bash
/backup/
├── apps/          # 容器应用备份
├── vms/           # 虚拟机备份
├── data/          # 重要数据备份
└── logs/          # 操作日志
```

### 自动化任务配置
```cron
# /etc/cron.d/auto-backup
0 2 * * 0 root /scripts/full-backup.sh    # 每周日全量备份
0 3 * * * root /scripts/incremental.sh   # 每日增量备份
```

## 🐳 容器应用备份
### 1Panel配置备份
```bash
# 手动备份命令
1pctl export --output /backup/apps/1panel-$(date +%Y%m%d).tar

# 自动恢复脚本
#!/bin/bash
1pctl import --file /backup/apps/1panel-latest.tar
docker-compose -f /opt/1panel/data/compose/**/*.yml up -d
```

### 容器数据卷备份
```bash
# 单容器备份
docker run --rm --volumes-from <容器ID> -v /backup:/backup busybox \
tar -czvf /backup/apps/volumes/<容器名>_$(date +%Y%m%d).tar.gz /容器数据路径

# 批量备份脚本
docker ps --format '{{.Names}}' | xargs -I {} sh -c \
'docker run --rm --volumes-from {} -v /backup:/backup busybox tar -czvf /backup/apps/volumes/{}_$(date +%Y%m%d).tar.gz /数据路径'
```

## 💻 虚拟机备份
### KVM热备份方案
```bash
# 创建快照
virsh snapshot-create-as --domain Win10 --disk-only --atomic

# 磁盘备份
rsync -avP --progress /var/lib/libvirt/images/Win10.qcow2 /backup/vms/Win10_$(date +%Y%m%d).qcow2

# 配置导出
virsh dumpxml Win10 > /backup/vms/Win10_config_$(date +%Y%m%d).xml
```

## 🔄 恢复操作手册
### 紧急恢复流程
1. ​**系统初始化**
```bash
fnos-init --restore-mode
mount /dev/sdb1 /mnt/backup
```

2. ​**容器恢复**
```bash
for img in /mnt/backup/apps/*.tar; do
  docker load -i $img
done
```

3. ​**虚拟机恢复**
```bash
virsh define /mnt/backup/vms/Win10_config.xml
virsh start Win10
```

## 🔐 安全加固方案
### 备份加密
```bash
# 使用Age加密
tar czvf - /data | age -e -r "age1xxxxxxxx" > /backup/data_encrypted_$(date +%Y%m%d).tar.gz.age

# 解密恢复
age -d -i ~/.age/key.txt /backup/data_encrypted_20240501.tar.gz.age | tar xzvf -
```

## 📊 监控看板配置
```yaml
# docker-compose-monitor.yml
version: '3'
services:
  prometheus:
    image: prom/prometheus
    volumes:
      - ./prometheus.yml:/etc/prometheus/prometheus.yml
    ports:
      - 9090:9090

  grafana:
    image: grafana/grafana
    ports:
      - 3000:3000
    environment:
      - GF_SECURITY_ADMIN_PASSWORD=备份监控密码
```

## 🚨 告警通知模板
```python
# /scripts/send_alert.py
import requests
def send_backup_alert(msg):
    webhook = "https://notification-server/alert"
    payload = {
        "system": "Backup System",
        "level": "CRITICAL",
        "message": msg
    }
    requests.post(webhook, json=payload)
```

> ​**操作提示**​
> 1. 首次部署后执行`/scripts/full-backup.sh test`验证流程
> 2. 每季度执行恢复演练
> 3. 备份密码建议存储在物理密码管理器中
```
