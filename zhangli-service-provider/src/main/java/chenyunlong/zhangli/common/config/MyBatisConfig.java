package chenyunlong.zhangli.common.config;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

/**
 * @author stan
 */
@Slf4j
//@Configuration
//@ConfigurationProperties(prefix = "mybatis")
public class MyBatisConfig {


    private final DataSource druidDataSource;


    @Value("${mapperLocations}")
    private String mapperLocations;


    public MyBatisConfig(DataSource druidDataSource) {
        this.druidDataSource = druidDataSource;
    }

    @Bean
    public SqlSessionFactoryBean sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(mapperLocations));
        log.info("sqlSessionFactory:--->mybatis.mapperLocation:" + mapperLocations);

        sqlSessionFactoryBean.setDataSource(druidDataSource);
        //configuration
        org.apache.ibatis.session.Configuration cfg = new org.apache.ibatis.session.Configuration();
        //设置相关参数，我这里就只用了一个
        log.info("sqlSessionFactoryBean:-->" + sqlSessionFactoryBean.getObject());
        sqlSessionFactoryBean.setConfiguration(cfg);
        return sqlSessionFactoryBean;
    }
}