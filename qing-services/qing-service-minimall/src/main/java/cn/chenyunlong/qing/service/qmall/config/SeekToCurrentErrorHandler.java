package cn.chenyunlong.qing.service.qmall.config;

import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.util.backoff.FixedBackOff;

public class SeekToCurrentErrorHandler implements CommonErrorHandler {
    public SeekToCurrentErrorHandler(DeadLetterPublishingRecoverer recoverer, FixedBackOff fixedBackOff) {

    }
}
