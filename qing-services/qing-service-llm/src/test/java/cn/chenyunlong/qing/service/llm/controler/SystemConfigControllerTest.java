package cn.chenyunlong.qing.service.llm.controler;

import cn.chenyunlong.qing.service.llm.service.SystemConfigService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class SystemConfigControllerTest {

    private MockMvc mockMvc;
    private SystemConfigService systemConfigService;

    @BeforeEach
    void setUp() {
        systemConfigService = mock(SystemConfigService.class);
        SystemConfigController controller = new SystemConfigController(systemConfigService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .build();
    }

    @Test
    @DisplayName("GET /api/finance/system-configs 返回配置列表")
    void shouldListAll() throws Exception {
        cn.chenyunlong.qing.service.llm.entity.SystemConfig cfg = new cn.chenyunlong.qing.service.llm.entity.SystemConfig();
        cfg.setConfigKey("dedup.timeToleranceMinutes");
        cfg.setConfigValue("5");
        when(systemConfigService.findAll()).thenReturn(List.of(cfg));

        mockMvc.perform(get("/api/finance/system-configs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data[0].configKey").value("dedup.timeToleranceMinutes"));
    }

    @Test
    @DisplayName("GET /api/finance/system-configs/{key} 返回单个配置")
    void shouldGetByKey() throws Exception {
        String value = systemConfigService.getValue("dedup.timeToleranceMinutes", "5");
        when(systemConfigService.getValue(eq("dedup.timeToleranceMinutes"), any())).thenReturn("10");

        mockMvc.perform(get("/api/finance/system-configs/{key}", "dedup.timeToleranceMinutes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value("10"));
    }

    @Test
    @DisplayName("PUT /api/finance/system-configs/{key} 设置配置值")
    void shouldSetValue() throws Exception {
        mockMvc.perform(put("/api/finance/system-configs/{key}", "dedup.timeToleranceMinutes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"value": "10", "description": "去重时间容忍"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        verify(systemConfigService).setValue("dedup.timeToleranceMinutes", "10", "去重时间容忍");
    }

    @Test
    @DisplayName("DELETE /api/finance/system-configs/{id} 删除配置")
    void shouldDelete() throws Exception {
        mockMvc.perform(delete("/api/finance/system-configs/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        verify(systemConfigService).delete(1L);
    }
}
