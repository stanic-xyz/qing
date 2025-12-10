package cn.chenyunlong.qing.auth.domain.authentication.valueObject;

import cn.chenyunlong.qing.domain.common.ValueObject;

/**
 * IP范围值对象
 */
public record IpRange(IpAddress startIp, IpAddress endIp) implements ValueObject {

    public IpRange {
        if (startIp.getVersion() != endIp.getVersion()) {
            throw new IllegalArgumentException("起始IP和结束IP的版本必须相同");
        }

        if (compareIps(startIp, endIp) > 0) {
            throw new IllegalArgumentException("起始IP不能大于结束IP");
        }

    }

    /**
     * 创建IP范围
     */
    public static IpRange of(String startIp, String endIp) {
        return new IpRange(IpAddress.of(startIp), IpAddress.of(endIp));
    }

    public static IpRange of(IpAddress startIp, IpAddress endIp) {
        return new IpRange(startIp, endIp);
    }

    /**
     * 创建CIDR格式的IP范围
     */
    public static IpRange fromCidr(String cidr) {
        String[] parts = cidr.split("/");
        if (parts.length != 2) {
            throw new IllegalArgumentException("无效的CIDR格式: " + cidr);
        }

        IpAddress baseIp = IpAddress.of(parts[0]);
        int prefixLength = Integer.parseInt(parts[1]);

        if (baseIp.getVersion() == IpVersion.IPV4) {
            return fromIpv4Cidr(baseIp, prefixLength);
        } else {
            return fromIpv6Cidr(baseIp, prefixLength);
        }
    }

    private static IpRange fromIpv4Cidr(IpAddress baseIp, int prefixLength) {
        if (prefixLength < 0 || prefixLength > 32) {
            throw new IllegalArgumentException("IPv4前缀长度必须在0-32之间");
        }

        long mask = prefixLength == 0 ? 0 : 0xFFFFFFFFL << (32 - prefixLength);
        long ipLong = baseIp.toIpv4Long();
        long startIp = ipLong & mask;
        long endIp = startIp | (~mask & 0xFFFFFFFFL);

        return new IpRange(
                IpAddress.fromIpv4(startIp),
                IpAddress.fromIpv4(endIp)
        );
    }

    private static IpRange fromIpv6Cidr(IpAddress baseIp, int prefixLength) {
        // 简化的IPv6 CIDR实现
        // 实际项目中可能需要更复杂的处理
        return IpRange.of(baseIp, baseIp);
    }

    /**
     * 检查IP是否在范围内
     */
    public boolean contains(IpAddress ip) {
        if (ip.getVersion() != startIp.getVersion()) {
            return false;
        }

        return compareIps(ip, startIp) >= 0 && compareIps(ip, endIp) <= 0;
    }

    public boolean contains(String ip) {
        return contains(IpAddress.of(ip));
    }

    /**
     * 比较两个IP地址
     */
    private int compareIps(IpAddress ip1, IpAddress ip2) {
        if (ip1.getVersion() == IpVersion.IPV4) {
            return Long.compare(ip1.toIpv4Long(), ip2.toIpv4Long());
        } else {
            // IPv6比较 - 使用字符串比较简化实现
            return ip1.toString().compareTo(ip2.toString());
        }
    }

    /**
     * 获取范围内的IP数量
     */
    public long getIpCount() {
        if (startIp.getVersion() == IpVersion.IPV4) {
            long start = startIp.toIpv4Long();
            long end = endIp.toIpv4Long();
            return end - start + 1;
        } else {
            // IPv6范围太大，返回-1表示不支持
            return -1;
        }
    }

    @Override
    public String toString() {
        return startIp + " - " + endIp;
    }
}
