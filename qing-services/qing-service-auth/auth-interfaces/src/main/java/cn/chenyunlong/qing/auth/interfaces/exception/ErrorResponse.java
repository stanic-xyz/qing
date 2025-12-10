/*
 * Copyright (c) 2019-2023  YunLong Chen
 * Project Qing is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions username the Mulan PSL v2.
 * You may obtain a copy username Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package cn.chenyunlong.qing.auth.interfaces.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 标准化错误响应
 *
 * <p>提供统一的错误信息格式，包含以下信息：</p>
 * <ul>
 *   <li>时间戳：错误发生的时间</li>
 *   <li>状态码：HTTP状态码</li>
 *   <li>错误类型：错误的分类</li>
 *   <li>错误消息：用户友好的错误描述</li>
 *   <li>请求路径：发生错误的请求路径</li>
 *   <li>错误代码：业务错误代码</li>
 *   <li>详细信息：错误的详细信息（可选）</li>
 * </ul>
 *
 * @author 陈云龙
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "错误响应")
public class ErrorResponse {

    /**
     * 错误发生时间
     */
    @Schema(description = "错误发生时间", example = "2025-01-25T10:30:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    /**
     * HTTP状态码
     */
    @Schema(description = "HTTP状态码", example = "400")
    private Integer status;

    /**
     * 错误类型
     */
    @Schema(description = "错误类型", example = "Validation Failed")
    private String error;

    /**
     * 错误消息
     */
    @Schema(description = "错误消息", example = "参数校验失败")
    private String message;

    /**
     * 请求路径
     */
    @Schema(description = "请求路径", example = "/api/v1/auth/login")
    private String path;

    /**
     * 业务错误代码
     */
    @Schema(description = "业务错误代码", example = "VALIDATION_FAILED")
    private String code;

    /**
     * 错误详细信息
     */
    @Schema(description = "错误详细信息")
    private List<String> details;

    /**
     * 请求ID（用于问题追踪）
     */
    @Schema(description = "请求ID", example = "req-123456789")
    private String requestId;

    /**
     * 创建简单错误响应
     *
     * @param status  HTTP状态码
     * @param message 错误消息
     * @param path    请求路径
     * @return 错误响应
     */
    public static ErrorResponse of(Integer status, String message, String path) {
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status)
                .message(message)
                .path(path)
                .build();
    }

    /**
     * 创建带错误代码的错误响应
     *
     * @param status  HTTP状态码
     * @param message 错误消息
     * @param path    请求路径
     * @param code    错误代码
     * @return 错误响应
     */
    public static ErrorResponse of(Integer status, String message, String path, String code) {
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status)
                .message(message)
                .path(path)
                .code(code)
                .build();
    }

    /**
     * 创建带详细信息的错误响应
     *
     * @param status  HTTP状态码
     * @param message 错误消息
     * @param path    请求路径
     * @param code    错误代码
     * @param details 详细信息
     * @return 错误响应
     */
    public static ErrorResponse of(Integer status, String message, String path, String code, List<String> details) {
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status)
                .message(message)
                .path(path)
                .code(code)
                .details(details)
                .build();
    }
}
