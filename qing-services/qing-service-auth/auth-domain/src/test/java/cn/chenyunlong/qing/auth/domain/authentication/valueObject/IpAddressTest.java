package cn.chenyunlong.qing.auth.domain.authentication.valueObject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IpAddressTest {

    @Test
    void shouldCreateValidIpv4Address() {
        IpAddress ip = IpAddress.of("192.168.1.1");

        assertEquals("192.168.1.1", ip.getValue());
        assertEquals(IpVersion.IPV4, ip.getVersion());
        assertTrue(ip.isPrivate());
    }

    @Test
    void shouldCreateValidIpv6Address() {
        assertThrowsExactly(IllegalArgumentException.class, () -> {
            IpAddress ip = IpAddress.of("2001:db8::1");
        }, "无效的IP地址格式: 2001:db8::1");
    }

    @Test
    void shouldThrowExceptionForInvalidIp() {
        assertThrows(IllegalArgumentException.class, () -> {
            IpAddress.of("invalid.ip.address");
        });
    }

    @Test
    void shouldConvertIpv4ToLong() {
        IpAddress ip = IpAddress.of("192.168.1.1");
        long ipLong = ip.toIpv4Long();

        assertEquals(3232235777L, ipLong);
    }

    @Test
    void shouldCreateFromIpv4Long() {
        IpAddress ip = IpAddress.fromIpv4(3232235777L);

        assertEquals("192.168.1.1", ip.getValue());
    }

    @Test
    void shouldMaskIpAddress() {
        IpAddress ip = IpAddress.of("192.168.1.100");
        String masked = ip.mask();

        assertEquals("192.168.xxx.xxx", masked);
    }
}
