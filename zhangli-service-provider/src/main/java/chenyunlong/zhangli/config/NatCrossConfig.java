package chenyunlong.zhangli.config;

import chenyunlong.zhangli.natcross.entity.ListenPort;
import chenyunlong.zhangli.natcross.server.NatcrossServer;
import chenyunlong.zhangli.natcross.service.IListenPortService;
import chenyunlong.zhangli.natcross.service.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import person.pluto.natcross2.serverside.client.ClientServiceThread;

import javax.annotation.PostConstruct;
import java.util.List;

@Configuration
public class NatCrossConfig {

    @Autowired
    private ClientServiceThread clientServiceThread;
    @Autowired
    private NatcrossServer natcrossServer;
    @Autowired
    private IListenPortService listenPortService;

    @PostConstruct
    public void start() throws Exception {

        //启动监听服务
        if (clientServiceThread != null) {
            //开启监听端口
            clientServiceThread.start();

            QueryWrapper<ListenPort> queryWrapper = new QueryWrapper<>();

            List<ListenPort> list = listenPortService.list(queryWrapper);

            //启动现在数据库中已有的服务
            for (ListenPort listenPort : list) {
                natcrossServer.createNewListen(listenPort);
            }
        }
    }
}
