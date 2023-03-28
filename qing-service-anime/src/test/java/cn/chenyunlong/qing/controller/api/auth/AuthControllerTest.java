/*
 * Copyright (c) 2019-2023  YunLong Chen
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

package cn.chenyunlong.qing.controller.api.auth;

import cn.chenyunlong.qing.controller.BaseApiTest;
import cn.chenyunlong.qing.infrastructure.model.params.LoginParam;
import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

class AuthControllerTest extends BaseApiTest {

    @Test
    void formLoin() throws Exception {

        LoginParam loginParam = new LoginParam();
        loginParam.setUsername("admin");
        loginParam.setPassword("123456");

        String content = JSONObject.toJSONString(loginParam);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/authorize/formLogin")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content);
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}
