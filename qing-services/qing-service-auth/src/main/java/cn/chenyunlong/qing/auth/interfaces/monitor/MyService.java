package cn.chenyunlong.qing.auth.interfaces.monitor;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class MyService {

    private final Counter myCounter;

    public MyService(MeterRegistry registry) {
        myCounter = Counter.builder("my_custom_counter")
                .description("A custom counter")
                .register(registry);
    }

    public void doSomething() {
        // 业务逻辑
        myCounter.increment();
    }
}
