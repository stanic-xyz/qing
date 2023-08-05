/*
 * Copyright (c) 2019-2023  YunLong Chen
 * Project Qing is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 *
 */

package cn.chenyunlong.qing.samples.codegen.domain.controller;

import cn.chenyunlong.qing.samples.codegen.QingCodegenSampleApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest(classes = QingCodegenSampleApplication.class)
@AutoConfigureMockMvc
class TestDomainControllerTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute(
            """
                insert into test_domain(id,username,password,created_at,updated_at) 
                values ('1','123','123','12313','12313')
                """);
    }

    @Test
    void page() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/test-domain/findByPage")
                .content("{}")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
            .andExpect(MockMvcResultMatchers.jsonPath("success").isBoolean())
            .andExpect(MockMvcResultMatchers.jsonPath("code").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("result.list").isArray())
            .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void findById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/test-domain/findById/1"))
            .andDo(MockMvcResultHandlers.print());
    }
}
