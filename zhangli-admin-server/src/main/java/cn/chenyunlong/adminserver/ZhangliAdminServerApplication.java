package cn.chenyunlong.adminserver;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Stan
 */
@EnableAdminServer
@SpringBootApplication
public class ZhangliAdminServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZhangliAdminServerApplication.class, args);
    }

}
