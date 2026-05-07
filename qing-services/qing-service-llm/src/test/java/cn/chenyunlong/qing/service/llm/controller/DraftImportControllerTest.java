package cn.chenyunlong.qing.service.llm.controller;

import cn.chenyunlong.qing.service.llm.controler.DraftImportController;
import cn.chenyunlong.qing.service.llm.dto.draft.DraftBatchResponse;
import cn.chenyunlong.qing.service.llm.entity.UnifiedDraftRecord;
import cn.chenyunlong.qing.service.llm.enums.AdapterTypeEnum;
import cn.chenyunlong.qing.service.llm.enums.DraftBatchStatusEnum;
import cn.chenyunlong.qing.service.llm.enums.DraftMatchStatusEnum;
import cn.chenyunlong.qing.service.llm.service.DraftBatchService;
import cn.chenyunlong.qing.service.llm.service.DraftRecordService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DraftImportController.class)
class DraftImportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DraftBatchService draftBatchService;

    @MockBean
    private DraftRecordService draftRecordService;

    @Test
    void createBatch_shouldReturnSuccessResult() throws Exception {
        DraftBatchResponse response = DraftBatchResponse.builder()
                .id(1L)
                .batchNo("udb-2026-05-07-1001")
                .adapterType(AdapterTypeEnum.PARSER)
                .status(DraftBatchStatusEnum.DRAFTED)
                .progress(0)
                .totalRecords(0)
                .allowedActions(List.of("START_MATCH"))
                .build();
        when(draftBatchService.create(any())).thenReturn(response);

        mockMvc.perform(post("/api/import/draft/batches")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.status").value("DRAFTED"));
    }

    @Test
    void doAction_shouldReturnUpdatedStatus() throws Exception {
        DraftBatchResponse response = DraftBatchResponse.builder()
                .id(1L)
                .batchNo("udb-2026-05-07-1001")
                .adapterType(AdapterTypeEnum.PARSER)
                .status(DraftBatchStatusEnum.MATCHING)
                .progress(20)
                .totalRecords(2)
                .allowedActions(List.of("REFRESH_STATUS"))
                .build();
        when(draftBatchService.changeStatus(eq(1L), eq(DraftBatchStatusEnum.MATCHING))).thenReturn(response);

        mockMvc.perform(post("/api/import/draft/batches/1/actions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(java.util.Map.of("targetStatus", "MATCHING"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.status").value("MATCHING"))
                .andExpect(jsonPath("$.data.progress").value(20));
    }

    @Test
    void pageRecords_shouldReturnPagePayload() throws Exception {
        UnifiedDraftRecord record = new UnifiedDraftRecord();
        record.setId(11L);
        record.setBatchId(1L);
        Page<UnifiedDraftRecord> page = new PageImpl<>(List.of(record), PageRequest.of(0, 20), 1);
        when(draftRecordService.pageByBatchId(1L, 0, 20, null)).thenReturn(page);

        mockMvc.perform(get("/api/import/draft/batches/1/records?page=0&size=20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.content[0].id").value(11))
                .andExpect(jsonPath("$.data.totalElements").value(1));
    }

    @Test
    void pageRecords_shouldSupportMatchStatusFilter() throws Exception {
        UnifiedDraftRecord record = new UnifiedDraftRecord();
        record.setId(22L);
        record.setBatchId(1L);
        record.setMatchStatus("MATCHED");
        Page<UnifiedDraftRecord> page = new PageImpl<>(List.of(record), PageRequest.of(0, 20), 1);
        when(draftRecordService.pageByBatchId(1L, 0, 20, DraftMatchStatusEnum.MATCHED)).thenReturn(page);

        mockMvc.perform(get("/api/import/draft/batches/1/records?page=0&size=20&matchStatus=MATCHED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.content[0].id").value(22))
                .andExpect(jsonPath("$.data.content[0].matchStatus").value("MATCHED"));
    }
}
