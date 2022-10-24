/*
 * Copyright (c) 2019-2022 YunLong Chen
 * Project Qing is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 *
 */

package cn.chenyunlong.nat.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import cn.chenyunlong.nat.entity.ListenPort;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Pluto
 * @since 2019-07-22 13:55:39
 */
@Mapper
@Component
public interface ListenPortMapper {

    /**
     * 获取监听的端口数量
     *
     * @return 监听端口数量
     */
    int count();

    /**
     * 获取所有端口简监听信息
     *
     * @return 端口监听列表
     */
    List<ListenPort> list();

    /**
     * 保存简监听端口信息
     *
     * @param listenPort 监听端口信息
     * @return 是否成功插入数据
     */
    Boolean save(ListenPort listenPort);

    /**
     * 根据监听端口获取端口监听信息
     *
     * @param listenPort 端口监听数据
     * @return 当前端口监听信息
     */
    ListenPort getByListenPort(Integer listenPort);

    /**
     * 根据ID删除端口监听信息
     *
     * @param listenPort 端口监听ID
     */
    void removeById(Integer listenPort);
}