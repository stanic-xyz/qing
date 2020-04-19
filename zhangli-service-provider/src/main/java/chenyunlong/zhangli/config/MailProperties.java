package chenyunlong.zhangli.config;

import org.springframework.context.annotation.Configuration;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
