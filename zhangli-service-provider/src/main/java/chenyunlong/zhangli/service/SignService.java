package chenyunlong.zhangli.service;

import chenyunlong.zhangli.entities.Sign;

public interface SignService {

    public int getSignStatus(Integer userId);

    void AddSign(Sign sign);
}
