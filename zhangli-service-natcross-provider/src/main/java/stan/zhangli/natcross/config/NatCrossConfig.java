package stan.zhangli.natcross.config;

import org.springframework.context.annotation.Configuration;
import person.pluto.natcross2.serverside.client.ClientServiceThread;
import stan.zhangli.natcross.entity.ListenPort;
import stan.zhangli.natcross.server.NatcrossServer;
import stan.zhangli.natcross.service.IListenPortService;

import javax.annotation.PostConstruct;
import java.util.List;

@Configuration
public class NatCrossConfig {

    private final ClientServiceThread clientServiceThread;
    private final NatcrossServer natcrossServer;
    private final IListenPortService listenPortService;

    public NatCrossConfig(ClientServiceThread clientServiceThread, NatcrossServer natcrossServer, IListenPortService listenPortService) {
        this.clientServiceThread = clientServiceThread;
        this.natcrossServer = natcrossServer;
        this.listenPortService = listenPortService;
    }

    @PostConstruct
    public void start() throws Exception {

        //启动监听服务
        if (clientServiceThread != null) {
            //开启监听端口
            clientServiceThread.start();

            List<ListenPort> list = listenPortService.list();
//            List<ListenPort> list = new LinkedList<>();

            //启动现在数据库中已有的服务
            for (ListenPort listenPort : list) {
                if (listenPort.getOnStart())
                    natcrossServer.createNewListen(listenPort);
            }
        }
    }
}
