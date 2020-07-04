package chenyunlong.zhangli.service.remote;


import chenyunlong.zhangli.api.IHelloService;

/**
 * 远程服务实现
 */
public class HelloServiceimpl implements IHelloService {
    @Override
    public String sayHello(String name) {
        return "Hello World:" + name;
    }
}
