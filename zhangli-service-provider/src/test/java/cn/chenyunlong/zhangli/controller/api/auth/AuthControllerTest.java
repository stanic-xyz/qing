package cn.chenyunlong.zhangli.controller.api.auth;

import cn.chenyunlong.zhangli.controller.BaseApiTest;
import cn.chenyunlong.zhangli.model.params.LoginParam;
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