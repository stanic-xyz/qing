package chenyunlong.zhangli.common.service;

import chenyunlong.zhangli.model.entities.Sign;

/**
 * @author Stan
 */
public interface SignService {


    /**
     * 获取签到状态
     *
     * @param userId 用户ID
     * @return 返回信息
     */
    int getSignStatus(Integer userId);

    /**
     * 添加签到信息
     *
     * @param sign 签到信息
     */
    void addSign(Sign sign);
}
