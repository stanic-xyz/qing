package cn.chenyunlong.qing.service.llm.controler;

import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.qing.service.llm.repository.TransactionRecordRepository;
import cn.chenyunlong.qing.service.llm.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

/**
 * 验证 CategoryController 删除接口在 Web 层的异常分流行为。
 */
class CategoryControllerWebLayerTest {

    private MockMvc mockMvc;

    private CategoryService categoryService;

    /**
     * 初始化真实 controller 的 standalone MockMvc，并挂载全局异常处理器。
     */
    @BeforeEach
    void setUp() {
        categoryService = mock(CategoryService.class);
        TransactionRecordRepository transactionRecordRepository = mock(TransactionRecordRepository.class);
        CategoryController controller = new CategoryController(categoryService, transactionRecordRepository);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalRestExceptionHandler())
                .build();
    }

    /**
     * 验证分类删除的已知业务错误会保留业务消息，并以统一 Result 结构返回。
     *
     * @throws Exception 测试执行异常
     */
    @Test
    void shouldKeepBusinessMessageWhenDeleteCategoryThrowsBusinessException() throws Exception {
        doThrow(new BusinessException("无法删除：该分类下存在子分类，请先删除子分类"))
                .when(categoryService)
                .deleteCategory(1L);

        mockMvc.perform(delete("/api/categories/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("无法删除：该分类下存在子分类，请先删除子分类"))
                .andExpect(jsonPath("$.data").value(nullValue()));
    }

    /**
     * 验证分类统计目标不存在时会交由全局异常处理器输出统一 404 结果。
     *
     * @throws Exception 测试执行异常
     */
    @Test
    void shouldReturnUnifiedResultWhenCategoryStatisticsTargetDoesNotExist() throws Exception {
        when(categoryService.getAllCategories()).thenReturn(List.of());

        mockMvc.perform(get("/api/categories/1/statistics"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.message").value("分类不存在"))
                .andExpect(jsonPath("$.data").value(nullValue()));
    }

    /**
     * 验证分类删除的未知异常会交由全局异常处理器兜底，且不会泄露底层消息。
     *
     * @throws Exception 测试执行异常
     */
    @Test
    void shouldHideUnknownMessageWhenDeleteCategoryThrowsUnexpectedRuntimeException() throws Exception {
        doThrow(new RuntimeException("底层 SQL 细节泄露"))
                .when(categoryService)
                .deleteCategory(1L);

        mockMvc.perform(delete("/api/categories/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.message").value("系统繁忙，请稍后重试"))
                .andExpect(jsonPath("$.message").value(not(containsString("底层 SQL 细节泄露"))))
                .andExpect(jsonPath("$.data").value(nullValue()));
    }
}
