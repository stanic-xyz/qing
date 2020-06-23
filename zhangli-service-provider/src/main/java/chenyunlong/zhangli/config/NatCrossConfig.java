package chenyunlong.zhangli.config;

import org.springframework.context.annotation.Configuration;
import person.pluto.natcross2.serverside.client.ClientServiceThread;

import javax.annotation.PostConstruct;

@Configuration
public class NatCrossConfig {

    private ClientServiceThread clientServiceThread;


    @PostConstruct
    public void start() throws Exception {
//        clientServiceThread.start();
    }
}
