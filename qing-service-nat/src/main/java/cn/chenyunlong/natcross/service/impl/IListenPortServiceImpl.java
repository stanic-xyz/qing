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

package cn.chenyunlong.natcross.service.impl;

import org.springframework.stereotype.Service;
import cn.chenyunlong.natcross.entity.ListenPort;
import cn.chenyunlong.natcross.mapper.ListenPortMapper;
import cn.chenyunlong.natcross.service.IListenPortService;

import java.util.List;

/**
 * @author Stan
 */
@Service
public class IListenPortServiceImpl implements IListenPortService {

    private final ListenPortMapper listenPortMapper;

    public IListenPortServiceImpl(ListenPortMapper listenPortMapper) {
        this.listenPortMapper = listenPortMapper;
    }

    @Override
    public int count() {
        return listenPortMapper.count();
    }

    @Override
    public boolean save(ListenPort listenPort) {
        return listenPortMapper.save(listenPort);
    }

    @Override
    public void removeById(Integer listenPort) {
        listenPortMapper.removeById(listenPort);
    }

    @Override
    public List<ListenPort> list() {
        return listenPortMapper.list();
    }

    @Override
    public ListenPort getByListenPort(Integer listenPort) {
        return listenPortMapper.getByListenPort(listenPort);
    }

    @Override
    public boolean updateById(ListenPort listenPort) {
        return false;
    }
}
