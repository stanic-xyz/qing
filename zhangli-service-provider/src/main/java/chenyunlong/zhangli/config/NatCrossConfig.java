package chenyunlong.zhangli.config;

import org.springframework.context.annotation.Configuration;
import person.pluto.natcross2.serverside.client.ClientServiceThread;

import javax.annotation.PostConstruct;

@Configuration
public class NatCrossConfig {

    private final ClientServiceThread clientServiceThread;

    public NatCrossConfig(ClientServiceThread clientServiceThread) {
        this.clientServiceThread = clientServiceThread;
    }


    @PostConstruct
    public void start() throws Exception {
        clientServiceThread.start();
    }
}
