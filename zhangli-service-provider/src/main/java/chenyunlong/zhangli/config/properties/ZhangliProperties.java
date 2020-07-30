package chenyunlong.zhangli.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
@ConfigurationProperties(prefix = "zhangli")
public class ZhangliProperties {
    private boolean openAopLog = true;
    private FileProperties file;
    private SecurityProperties security;
    private SwaggerProperties swagger;

    public ZhangliProperties() {
    }

    public ZhangliProperties(boolean openAopLog, FileProperties file, SecurityProperties security, SwaggerProperties swagger, String authticationPrefix) {
        this.openAopLog = openAopLog;
        this.file = file;
        this.security = security;
        this.swagger = swagger;
    }

    public boolean isOpenAopLog() {
        return openAopLog;
    }

    public void setOpenAopLog(boolean openAopLog) {
        this.openAopLog = openAopLog;
    }

    public FileProperties getFile() {
        return file;
    }

    public void setFile(FileProperties file) {
        this.file = file;
    }

    public SecurityProperties getSecurity() {
        return security;
    }

    public void setSecurity(SecurityProperties security) {
        this.security = security;
    }

    public SwaggerProperties getSwagger() {
        return swagger;
    }

    public void setSwagger(SwaggerProperties swagger) {
        this.swagger = swagger;
    }
}

