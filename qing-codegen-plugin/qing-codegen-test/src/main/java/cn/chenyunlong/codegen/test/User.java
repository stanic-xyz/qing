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

package cn.chenyunlong.codegen.test;

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
@GenVo(pkgName = "com.only4play.codegen.test.vo")
@GenCreator(pkgName = "com.only4play.codegen.test.creator")
@GenUpdater(pkgName = "com.only4play.codegen.test.updater")
@GenRepository(pkgName = "com.only4play.codegen.test.repository")
@GenService(pkgName = "com.only4play.codegen.test.service")
@GenController(pkgName = "com.only4play.codegen.test.controller")
@GenQuery(pkgName = "com.only4play.codegen.test.query")
@GenUpdateRequest(pkgName = "com.only4play.codegen.test.api.request")
@GenQueryRequest(pkgName = "com.only4play.codegen.test.api.request")
@GenResponse(pkgName = "com.only4play.codegen.test.api.response")
@GenFeign(pkgName = "com.only4play.codegen.test.api.service")
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
