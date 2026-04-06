package cn.chenyunlong.qing.service.llm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.scheduling.annotation.EnableAsync;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@EnableAsync
@SpringBootApplication
//@EnableCaching
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
public class QingServiceLlmApplication {

    public static void main(String[] args) {
        SpringApplication.run(QingServiceLlmApplication.class, args);
    }
}
