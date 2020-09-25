package chenyunlong.zhangli.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.util.logging.Logger;

/**
 * @author stan
 */
@Configuration
@ConfigurationProperties(prefix = "mybatis")
@PropertySource("classpath:application-mybatis.yml")
public class MyBatisConfig {

    private static final Logger logger = Logger.getLogger(String.valueOf(MyBatisConfig.class));

    private final DataSource druidDataSource;


    @Value("${mapperLocations}")
    private String mapperLocations;

    @Value("${default-statement-timeout}")
    private int dst;

    public MyBatisConfig(DataSource druidDataSource) {
        this.druidDataSource = druidDataSource;
    }

    @Bean
    public SqlSessionFactoryBean sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(mapperLocations));
        logger.info("sqlSessionFactory:--->mybatis.mapperLocation:" + mapperLocations);

        sqlSessionFactoryBean.setDataSource(druidDataSource);
        //configuration
        org.apache.ibatis.session.Configuration cfg = new org.apache.ibatis.session.Configuration();
        //设置相关参数，我这里就只用了一个
        cfg.setDefaultStatementTimeout(dst);
        logger.info("sqlSessionFactoryBean:-->" + sqlSessionFactoryBean.getObject());
        logger.info("default-statement-timeout:" + dst);
        sqlSessionFactoryBean.setConfiguration(cfg);
        return sqlSessionFactoryBean;
    }
}