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
 */

package cn.chenyunlong.natcross.service;

import cn.chenyunlong.natcross.entity.ListenPort;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Pluto
 * @since 2019-07-22 13:55:39
 */
public interface IListenPortService {


    int count();

    boolean save(ListenPort listenPort);

    void removeById(Integer listenPort);

    List<ListenPort> list();

    ListenPort getByListenPort(Integer listenPort);

    boolean updateById(ListenPort listenPort);
}
