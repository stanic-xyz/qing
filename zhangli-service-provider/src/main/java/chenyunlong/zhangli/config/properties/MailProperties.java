package chenyunlong.zhangli.config.properties;

import lombok.Data;
import org.springframework.context.annotation.Configuration;

/**
 * @author Stan
 */
@Data
@Configuration
public class MailProperties {
    private String username;

    private String password;

    public MailProperties() {
    }

    public MailProperties(String username, String password) {
        this.username = username;
        this.password = password;
    }

}
