package cn.chenyunlong.qing.domain.file.controller;

import cn.chenyunlong.qing.domain.AbstractDomainTests;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@Slf4j
@Rollback
@AutoConfigureMockMvc
class AttachementControllerTest extends AbstractDomainTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Rollback
    void createUploadFile() throws Exception {
        log.info("测试上传文件");
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/upload-file")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                    "fileId": "123123",
                    "fileName": "filename"
                    }
                    """)
            )
            .andDo(MockMvcResultHandlers.log())
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(1));
    }

    @Test
    @Rollback
    void findById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/upload-file/findById/123123"))
            .andDo(MockMvcResultHandlers.log())
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }
}
