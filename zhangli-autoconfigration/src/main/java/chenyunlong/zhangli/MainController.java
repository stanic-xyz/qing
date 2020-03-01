package chenyunlong.zhangli;


import chenyunlong.zhangli.config.ZhangliConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@Slf4j
@SpringBootApplication
public class MainController {

    @Autowired
    private ZhangliConfig zhangliConfig;

    public static void main(String[] args) {
        SpringApplication.run(MainController.class, args);

    }
}