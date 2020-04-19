package chenyunlong.zhangli.properties;

import chenyunlong.zhangli.config.FileConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
@ConfigurationProperties(prefix = "zhangli")
public class ZhangliProperties {
    private boolean openAopLog = true;
    private FileConfigurationProperties file;
    private ShiroProperties shiro;
    private SwaggerProperties swagger = new SwaggerProperties();

    public ZhangliProperties() {
    }

    public ZhangliProperties(boolean openAopLog, FileConfigurationProperties file, ShiroProperties shiro, SwaggerProperties swagger) {
        this.openAopLog = openAopLog;
        this.file = file;
        this.shiro = shiro;
        this.swagger = swagger;
    }

    public boolean isOpenAopLog() {
        return openAopLog;
    }

    public void setOpenAopLog(boolean openAopLog) {
        this.openAopLog = openAopLog;
    }

    public FileConfigurationProperties getFile() {
        return file;
    }

    public void setFile(FileConfigurationProperties file) {
        this.file = file;
    }

    public ShiroProperties getShiro() {
        return shiro;
    }

    public void setShiro(ShiroProperties shiro) {
        this.shiro = shiro;
    }

    public SwaggerProperties getSwagger() {
        return swagger;
    }

    public void setSwagger(SwaggerProperties swagger) {
        this.swagger = swagger;
    }

    @Override
    public String toString() {
        return "ZhangliProperties{" +
                "openAopLog=" + openAopLog +
                ", file=" + file +
                ", shiro=" + shiro +
                ", swagger=" + swagger +
                '}';
    }
}

