package chenyunlong.zhangli.service;

import chenyunlong.zhangli.model.vo.AnimeOptionsModel;

/**
 * 选项服务
 *
 * @author Stan
 */
public interface AnimeOptionsService {
    /**
     * @return 获取所有筛选条件
     */
    AnimeOptionsModel getOptions();
}