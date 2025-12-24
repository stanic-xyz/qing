#!/bin/bash

# 从环境变量获取参数
MQTT_BROKER=${MQTT_BROKER:-localhost}
MQTT_PORT=${MQTT_PORT:-1883}
MQTT_USERNAME=${MQTT_USERNAME:-}
MQTT_PASSWORD=${MQTT_PASSWORD:-}
MONITOR_INTERVAL=${MONITOR_INTERVAL:-30}
TEMP_SENSORS=${TEMP_SENSORS:-"CPU_Temp System_Temp"}
FAN_CONTROL_ENABLED=${FAN_CONTROL_ENABLED:-true}
MIN_TEMP=${MIN_TEMP:-30}
MAX_TEMP=${MAX_TEMP:-70}
MIN_FAN_SPEED=${MIN_FAN_SPEED:-20}
MAX_FAN_SPEED=${MAX_FAN_SPEED:-100}

# MQTT 发布函数
mqtt_publish() {
    local topic=$1
    local message=$2
    mosquitto_pub -h $MQTT_BROKER -p $MQTT_PORT \
        -u "$MQTT_USERNAME" -P "$MQTT_PASSWORD" \
        -t "$topic" -m "$message"
}

# 设置风扇速度为百分比
set_fan_speed() {
    local speed=$1
    # 转换为十六进制值 (0x00 到 0x64)
    local hex_speed=$(printf "0x%x" $speed)

    # 超微主板风扇控制命令 (不同主板需调整)
    ipmitool raw 0x30 0x30 0x02 0xff $hex_speed
}

# 主监控循环
while true; do
    # 获取所有传感器数据
    sdr_output=$(ipmitool sdr)

    # 处理每个温度传感器
    for sensor in $TEMP_SENSORS; do
        # 提取温度值
        temp=$(echo "$sdr_output" | grep "$sensor" | awk '{print $3}')

        if [ -n "$temp" ]; then
            # 发布到MQTT
            mqtt_publish "server/sensors/$sensor/temperature" "$temp"

            # 如果启用了风扇控制
            if [ "$FAN_CONTROL_ENABLED" = "true" ]; then
                # 计算所需风扇速度 (线性插值)
                if (( $(echo "$temp <= $MIN_TEMP" | bc -l) )); then
                    fan_speed=$MIN_FAN_SPEED
                elif (( $(echo "$temp >= $MAX_TEMP" | bc -l) )); then
                    fan_speed=$MAX_FAN_SPEED
                else
                    range=$(($MAX_TEMP - $MIN_TEMP))
                    percent=$(bc <<< "scale=2; ($temp - $MIN_TEMP) / $range * 100")
                    fan_speed=$(bc <<< "scale=0; $MIN_FAN_SPEED + ($MAX_FAN_SPEED - $MIN_FAN_SPEED) * $percent / 100")
                    fan_speed=${fan_speed%.*}  # 取整
                fi

                # 设置风扇速度
                set_fan_speed $fan_speed
                mqtt_publish "server/fan/speed" "$fan_speed"
                mqtt_publish "server/fan/status" "Auto: $fan_speed% ($temp°C)"
            fi
        fi
    done

    sleep $MONITOR_INTERVAL
done
