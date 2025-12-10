package cn.chenyunlong.qing.auth.domain.authentication.valueObject;

import lombok.Getter;

/**
 * IP版本枚举
 */
@Getter
public enum IpVersion {
    IPV4("IPv4"),
    IPV6("IPv6");

    private final String description;

    IpVersion(String description) {
        this.description = description;
    }

}
