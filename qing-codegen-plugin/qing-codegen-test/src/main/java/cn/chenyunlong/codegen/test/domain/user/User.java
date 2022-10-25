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

package cn.chenyunlong.codegen.test.domain.user;

import cn.chenyunlong.codegen.processor.api.GenFeign;
import cn.chenyunlong.codegen.processor.api.GenQueryRequest;
import cn.chenyunlong.codegen.processor.api.GenResponse;
import cn.chenyunlong.codegen.processor.api.GenUpdateRequest;
import cn.chenyunlong.codegen.processor.controller.GenController;
import cn.chenyunlong.codegen.processor.creator.GenCreator;
import cn.chenyunlong.codegen.processor.query.GenQuery;
import cn.chenyunlong.codegen.processor.repository.GenRepository;
import cn.chenyunlong.codegen.processor.service.GenService;
import cn.chenyunlong.codegen.processor.updater.GenUpdater;
import cn.chenyunlong.codegen.processor.vo.GenVo;
import com.only4play.jpa.support.BaseJpaAggregate;

/**
 * @author gim 2022/1/11 10:53 下午
 */
//
@GenVo(pkgName = "cn.chenyunlong.codegen.test.domain.user.vo")
@GenCreator(pkgName = "cn.chenyunlong.codegen.test.domain.user.creator")
@GenUpdater(pkgName = "cn.chenyunlong.codegen.test.domain.user.updater")
@GenRepository(pkgName = "cn.chenyunlong.codegen.test.domain.user.repository")
@GenService(pkgName = "cn.chenyunlong.codegen.test.domain.user.service")
@GenController(pkgName = "cn.chenyunlong.codegen.test.domain.user.controller")
@GenQuery(pkgName = "cn.chenyunlong.codegen.test.domain.user.query")
@GenUpdateRequest(pkgName = "cn.chenyunlong.codegen.test.domain.user.api.request")
@GenQueryRequest(pkgName = "cn.chenyunlong.codegen.test.domain.user.api.request")
@GenResponse(pkgName = "cn.chenyunlong.codegen.test.domain.user.api.response")
@GenFeign(pkgName = "cn.chenyunlong.codegen.test.domain.user.api.service")
public class User extends BaseJpaAggregate {

    private String username;

    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void valid() {
    }

    public void invalid() {
    }
}
