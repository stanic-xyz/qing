package cn.chenyunlong.zhangli.service.impl;

import cn.chenyunlong.zhangli.model.entities.Sign;
import cn.chenyunlong.zhangli.mapper.SignMapper;
import cn.chenyunlong.zhangli.service.SignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Stan
 */
@Service
public class SignServiceImpl implements SignService {

    @Autowired
    private SignMapper signMapper;


    @Override
    public int getSignStatus(Integer userId) {
        return signMapper.getSignState(userId);
    }

    @Override
    public void addSign(Sign sign) {
        signMapper.add(sign);
    }
}
