package chenyunlong.zhangli.gateway;

import chenyunlong.zhangli.gateway.filter.TokenFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

@EnableZuulProxy
@EnableDiscoveryClient
@SpringBootApplication
public class ZhangliZuulClientApplication9001 {

    public static void main(String[] args) {
        SpringApplication.run(ZhangliZuulClientApplication9001.class, args);
    }

//    @Bean
//    TokenFilter getZuulFilter() {
//        return new TokenFilter();
//    }
}
