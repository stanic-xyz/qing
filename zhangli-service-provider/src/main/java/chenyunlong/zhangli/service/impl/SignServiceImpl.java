package chenyunlong.zhangli.service.impl;

import chenyunlong.zhangli.entities.Sign;
import chenyunlong.zhangli.mapper.SignMapper;
import chenyunlong.zhangli.service.SignService;
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
