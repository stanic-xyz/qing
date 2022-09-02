package cn.chenyunlong.zhangli.service;

import cn.chenyunlong.zhangli.model.vo.OptionsModel;

/**
 * 选项服务
 *
 * @author Stan
 */
public interface AnimeOptionsService {
    /**
     * 获取选项信息
     *
     * @return 获取所有筛选条件
     */
    OptionsModel getOptions();

    /**
     * 获取最近列表的默认分页大小
     *
     * @return 最新记录的分页大小
     */
    int getRecentPageSize();


    /**
     * 更新配置信息
     */
    void updateOptions();
}