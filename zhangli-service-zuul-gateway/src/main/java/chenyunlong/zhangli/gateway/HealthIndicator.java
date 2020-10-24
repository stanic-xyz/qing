package chenyunlong.zhangli.gateway;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;

class ConnectTimeHealthIndicator implements HealthIndicator {
    @Override
    public Health health() {
        long connectTime = (long) (Math.random() * 100);

        if (connectTime >= 3) {
            return Health.down().withDetail("code", "504").withDetail("msg", "应用链接超时").build();
        } else {
            return Health.up().build();
        }
    }
}
