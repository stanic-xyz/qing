package cn.chenyunlong.qing.auth.domain.authentication.valueObject;

import cn.chenyunlong.qing.domain.common.ValueObject;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.util.Assert;

import java.util.regex.Pattern;

/**
 * IP地址值对象
 * 封装IP地址的业务规则和验证逻辑
 */
@Getter
@EqualsAndHashCode
public class IpAddress implements ValueObject {

    private static final Pattern IPV4_PATTERN = Pattern.compile(
            "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$"
    );

    private static final Pattern IPV6_PATTERN = Pattern.compile(
            "^([0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$|^::1$|^::$"
    );

    private final String value;
    private final IpVersion version;
    private final boolean isPrivate;

    /**
     * 私有构造函数
     */
    private IpAddress(String ipAddress) {
        Assert.hasText(ipAddress, "IP地址不能为空");

        String trimmedIp = ipAddress.trim();
        validateIpAddress(trimmedIp);

        this.value = trimmedIp;
        this.version = determineIpVersion(trimmedIp);
        this.isPrivate = checkIfPrivate(trimmedIp, this.version);
    }

    /**
     * 静态工厂方法
     */
    public static IpAddress of(String ipAddress) {
        return new IpAddress(ipAddress);
    }

    /**
     * 从IPv4整数值创建
     */
    public static IpAddress fromIpv4(long ipv4Value) {
        if (ipv4Value < 0 || ipv4Value > 0xFFFFFFFFL) {
            throw new IllegalArgumentException("IPv4值必须在0-4294967295之间");
        }

        String ip = String.format("%d.%d.%d.%d",
                (ipv4Value >>> 24) & 0xFF,
                (ipv4Value >>> 16) & 0xFF,
                (ipv4Value >>> 8) & 0xFF,
                ipv4Value & 0xFF
        );

        return new IpAddress(ip);
    }

    /**
     * 验证IP地址格式
     */
    private void validateIpAddress(String ipAddress) {
        boolean isValid = IPV4_PATTERN.matcher(ipAddress).matches() || IPV6_PATTERN.matcher(ipAddress).matches();

        if (!isValid) {
            throw new IllegalArgumentException("无效的IP地址格式: " + ipAddress);
        }
    }

    /**
     * 判断IP版本
     */
    private IpVersion determineIpVersion(String ipAddress) {
        return ipAddress.contains(":") ? IpVersion.IPV6 : IpVersion.IPV4;
    }

    /**
     * 检查是否为私有IP
     */
    private boolean checkIfPrivate(String ipAddress, IpVersion version) {
        if (version == IpVersion.IPV4) {
            return isPrivateIpv4(ipAddress);
        } else {
            return isPrivateIpv6(ipAddress);
        }
    }

    /**
     * 检查IPv4是否为私有地址
     */
    private boolean isPrivateIpv4(String ipAddress) {
        // 10.0.0.0/8
        if (ipAddress.startsWith("10.")) {
            return true;
        }

        // 172.16.0.0/12
        if (ipAddress.startsWith("172.")) {
            String[] parts = ipAddress.split("\\.");
            if (parts.length >= 2) {
                int second = Integer.parseInt(parts[1]);
                if (second >= 16 && second <= 31) {
                    return true;
                }
            }
        }

        // 192.168.0.0/16
        if (ipAddress.startsWith("192.168.")) {
            return true;
        }

        // 127.0.0.0/8 (环回地址)
        if (ipAddress.startsWith("127.")) {
            return true;
        }

        // 169.254.0.0/16 (链路本地)
        return ipAddress.startsWith("169.254.");
    }

    /**
     * 检查IPv6是否为私有地址
     */
    private boolean isPrivateIpv6(String ipAddress) {
        // 环回地址
        if ("::1".equals(ipAddress)) {
            return true;
        }

        // 链路本地地址
        if (ipAddress.startsWith("fe80:")) {
            return true;
        }

        // 唯一本地地址 (ULA) - fc00::/7
        return ipAddress.startsWith("fc") || ipAddress.startsWith("fd");
    }

    /**
     * 转换为IPv4整数值
     */
    public long toIpv4Long() {
        if (version != IpVersion.IPV4) {
            throw new IllegalStateException("只有IPv4地址可以转换为长整型");
        }

        String[] parts = value.split("\\.");
        long result = 0;
        for (int i = 0; i < 4; i++) {
            result = result << 8 | Integer.parseInt(parts[i]);
        }
        return result;
    }

    /**
     * 获取IP段
     */
    public String getIpSegment(int segment) {
        String[] parts = value.split(version == IpVersion.IPV4 ? "\\." : ":");
        if (segment < 0 || segment >= parts.length) {
            throw new IllegalArgumentException("无效的IP段索引: " + segment);
        }
        return parts[segment];
    }

    /**
     * 是否为环回地址
     */
    public boolean isLoopback() {
        if (version == IpVersion.IPV4) {
            return value.startsWith("127.");
        } else {
            return "::1".equals(value);
        }
    }

    /**
     * 是否为链路本地地址
     */
    public boolean isLinkLocal() {
        if (version == IpVersion.IPV4) {
            return value.startsWith("169.254.");
        } else {
            return value.startsWith("fe80:");
        }
    }

    /**
     * 脱敏处理（用于日志等场景）
     */
    public String mask() {
        if (version == IpVersion.IPV4) {
            String[] parts = value.split("\\.");
            return parts[0] + "." + parts[1] + ".xxx.xxx";
        } else {
            // IPv6脱敏：保留前两段
            String[] parts = value.split(":");
            if (parts.length >= 2) {
                return parts[0] + ":" + parts[1] + ":xxxx:xxxx:xxxx:xxxx:xxxx:xxxx";
            }
            return "xxxx:xxxx:xxxx:xxxx:xxxx:xxxx:xxxx:xxxx";
        }
    }

    @Override
    public String toString() {
        return value;
    }
}
