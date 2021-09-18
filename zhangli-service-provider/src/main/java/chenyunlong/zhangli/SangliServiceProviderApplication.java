package chenyunlong.zhangli;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

/**
 * @author Stan
 */
@EnableCaching
@EnableDiscoveryClient
@SpringBootApplication
public class SangliServiceProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(SangliServiceProviderApplication.class, args);
    }

    @Bean("tttt")
    public String test(SqlSessionFactory sqlSessionFactory) {


        SqlSession sqlSession = sqlSessionFactory.openSession();
        return String.join(",", sqlSessionFactory.getConfiguration().getCacheNames());
    }

}