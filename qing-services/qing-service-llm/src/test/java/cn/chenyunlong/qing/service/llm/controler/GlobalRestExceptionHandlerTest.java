package cn.chenyunlong.qing.service.llm.controler;

import cn.chenyunlong.common.exception.BadRequestException;
import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.common.exception.NotFoundException;
import cn.chenyunlong.qing.service.llm.exception.ConflictException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 验证全局异常处理器会为 REST 接口统一输出 Result 结构。
 */
class GlobalRestExceptionHandlerTest {

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 初始化 standalone MockMvc，直接验证 controller + advice 的 Web 层行为。
     */
    @BeforeEach
    void setUp() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();
        mockMvc = MockMvcBuilders.standaloneSetup(new TestExceptionController())
                .setControllerAdvice(new GlobalRestExceptionHandler())
                .setValidator(validator)
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();
    }

    /**
     * 校验 BusinessException 会被映射为统一错误结果。
     *
     * @throws Exception 测试执行异常
     */
    @Test
    void shouldReturnUnifiedResultForBusinessException() throws Exception {
        mockMvc.perform(get("/api/test/exceptions/business"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("业务异常测试"))
                .andExpect(jsonPath("$.data").value(nullValue()));
    }

    /**
     * 校验 BadRequestException 会被映射为统一参数错误结果。
     *
     * @throws Exception 测试执行异常
     */
    @Test
    void shouldReturnUnifiedResultForBadRequestException() throws Exception {
        mockMvc.perform(get("/api/test/exceptions/bad-request"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("参数不合法测试"))
                .andExpect(jsonPath("$.data").value(nullValue()));
    }

    /**
     * 校验 NotFoundException 会被映射为统一资源不存在结果。
     *
     * @throws Exception 测试执行异常
     */
    @Test
    void shouldReturnUnifiedResultForNotFoundException() throws Exception {
        mockMvc.perform(get("/api/test/exceptions/not-found"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.message").value("资源不存在测试"))
                .andExpect(jsonPath("$.data").value(nullValue()));
    }

    /**
     * 校验 ConflictException 会被映射为统一业务冲突结果。
     *
     * @throws Exception 测试执行异常
     */
    @Test
    void shouldReturnUnifiedResultForConflictException() throws Exception {
        mockMvc.perform(get("/api/test/exceptions/conflict"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(409))
                .andExpect(jsonPath("$.message").value("业务冲突测试"))
                .andExpect(jsonPath("$.data").value(nullValue()));
    }

    /**
     * 校验 IllegalStateException 仍按现有系统兼容语义映射为业务错误结果。
     *
     * @throws Exception 测试执行异常
     */
    @Test
    void shouldReturnUnifiedResultForIllegalStateException() throws Exception {
        mockMvc.perform(get("/api/test/exceptions/illegal-state"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("状态不允许测试"))
                .andExpect(jsonPath("$.data").value(nullValue()));
    }

    /**
     * 校验请求体校验失败时返回统一错误结果。
     *
     * @throws Exception 测试执行异常
     */
    @Test
    void shouldReturnUnifiedResultForRequestBodyValidationFailure() throws Exception {
        TestRequest request = new TestRequest();
        request.setName("");

        mockMvc.perform(post("/api/test/exceptions/validation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("name: name不能为空"))
                .andExpect(jsonPath("$.data").value(nullValue()));
    }

    /**
     * 校验未知异常会被受控文案兜底且保持统一 Result 结构。
     *
     * @throws Exception 测试执行异常
     */
    @Test
    void shouldReturnUnifiedResultForUnknownException() throws Exception {
        mockMvc.perform(get("/api/test/exceptions/unknown"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.message").value("系统繁忙，请稍后重试"))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("敏感异常信息"))))
                .andExpect(jsonPath("$.data").value(nullValue()));
    }

    /**
     * 专门用于触发异常场景的测试控制器。
     */
    @RestController
    @RequestMapping("/api/test/exceptions")
    public static class TestExceptionController {

        /**
         * 触发业务异常。
         *
         * @return 永不返回
         */
        @GetMapping("/business")
        public String throwBusinessException() {
            throw new BusinessException("业务异常测试");
        }

        /**
         * 触发参数异常。
         *
         * @return 永不返回
         */
        @GetMapping("/bad-request")
        public String throwBadRequestException() {
            throw new BadRequestException("参数不合法测试");
        }

        /**
         * 触发资源不存在异常。
         *
         * @return 永不返回
         */
        @GetMapping("/not-found")
        public String throwNotFoundException() {
            throw new NotFoundException("资源不存在测试");
        }

        /**
         * 触发业务冲突异常。
         *
         * @return 永不返回
         */
        @GetMapping("/conflict")
        public String throwConflictException() {
            throw new ConflictException("业务冲突测试");
        }

        /**
         * 触发非法状态异常。
         *
         * @return 永不返回
         */
        @GetMapping("/illegal-state")
        public String throwIllegalStateException() {
            throw new IllegalStateException("状态不允许测试");
        }

        /**
         * 触发请求体验证异常。
         *
         * @param request 测试请求体
         * @return 成功占位结果
         */
        @PostMapping("/validation")
        public String validateRequest(@RequestBody @Valid TestRequest request) {
            return "ok";
        }

        /**
         * 触发未知异常。
         *
         * @return 永不返回
         */
        @GetMapping("/unknown")
        public String throwUnknownException() {
            throw new RuntimeException("敏感异常信息");
        }
    }

    /**
     * 测试用请求体。
     */
    public static class TestRequest {

        @NotBlank(message = "name不能为空")
        private String name;

        /**
         * 获取名称。
         *
         * @return 名称
         */
        public String getName() {
            return name;
        }

        /**
         * 设置名称。
         *
         * @param name 名称
         */
        public void setName(String name) {
            this.name = name;
        }
    }
}
