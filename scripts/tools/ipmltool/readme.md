首先开启主板上的ipmi 设置好 用户名密码
此过程可以在网上搜索，有很多的资料可以参考

然后下载 [https://github.com/cw1997/dell_fans_controller](https://github.com/cw1997/dell_fans_controller)

https://github.com/cw1997/dell_fans_controller/releases/tag/v1.0.0

解压只有在目录里面有ipmltool `Dell\SysMgt\bmc\ipmitool.exe`

首先停止服务器的自动风扇控制，最后一位0x00表示停止自动风扇控制，0x01为开启自动风扇控制。

```shell
ipmitool -I lanplus -H idrac控制地址 -U 用户名 -P 密码 raw 0x30 0x30 0x01 0x00

```

然后手动设置风扇，最后一位0x1e为设置30%的转速（16进制）。最后4位 0x0f 表示15%，0x0a，表示10%，不建议小于10%，因为10%已经很静音了 如果再小怕不能控温，烧毁硬件。

```shell
ipmitool -I lanplus -H idrac控制地址-U 用户名 -P 密码 raw 0x30 0x30 0x02 0xff 0x1e
```

下面是一些常用的修改温度命令

```shell
# 查看服务器温度
ipmitool -I lanplus -H <iDRAC-IP> -U <iDRAC-USER> -P <iDRAC-PASSWORD> sensor | grep Temp
# 禁用风扇自动控制
ipmitool -I lanplus -H <iDRAC-IP> -U <iDRAC-USER> -P <iDRAC-PASSWORD> raw 0x30 0x30 0x01 0x00
# 启用风扇自动控制
ipmitool -I lanplus -H <iDRAC-IP> -U <iDRAC-USER> -P <iDRAC-PASSWORD> raw 0x30 0x30 0x01 0x01
# 设置风扇转速0%
ipmitool -I lanplus -H <iDRAC-IP> -U <iDRAC-USER> -P <iDRAC-PASSWORD> raw 0x30 0x30 0x02 0xff 0x00
# 设置风扇转速10%
ipmitool -I lanplus -H <iDRAC-IP> -U <iDRAC-USER> -P <iDRAC-PASSWORD> raw 0x30 0x30 0x02 0xff 0x0a
# 设置风扇转速20%
ipmitool -I lanplus -H <iDRAC-IP> -U <iDRAC-USER> -P <iDRAC-PASSWORD> raw 0x30 0x30 0x02 0xff 0x14
# 设置风扇转速30%
ipmitool -I lanplus -H <iDRAC-IP> -U <iDRAC-USER> -P <iDRAC-PASSWORD> raw 0x30 0x30 0x02 0xff 0x1e
# 设置风扇转速100%
ipmitool -I lanplus -H <iDRAC-IP> -U <iDRAC-USER> -P <iDRAC-PASSWORD> raw 0x30 0x30 0x02 0xff 0x64

#设置风扇转速的命令的最后一个参数就是你需要控制的风扇百分比的16进制值

```
