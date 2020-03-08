package chenyunlong.zhangli;


import chenyunlong.zhangli.config.ZhangliConfig;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.URLUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.URL;

@Slf4j
@SpringBootApplication
public class MainController {

    @Autowired
    private ZhangliConfig zhangliConfig;

    public static void main(String[] args) {
        SpringApplication.run(MainController.class, args);

        URL url = URLUtil.url("http://www.chenyunlong.cn");
        log.info(String.valueOf(url));
    }
}