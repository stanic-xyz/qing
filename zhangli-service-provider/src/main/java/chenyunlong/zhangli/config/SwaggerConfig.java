package chenyunlong.zhangli.config;

import chenyunlong.zhangli.config.properties.SwaggerProperties;
import chenyunlong.zhangli.config.properties.ZhangliProperties;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger配置信息
 *
 * @author Stan
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig extends BaseSwaggerConfig {

    private final ZhangliProperties zhangliProperties;

    public SwaggerConfig(ZhangliProperties zhangliProperties) {
        this.zhangliProperties = zhangliProperties;
    }

    @Override
    public SwaggerProperties swaggerProperties() {
        return zhangliProperties.getSwagger();
    }
}
