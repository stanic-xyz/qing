package cn.chenyunlong.qing.anime.interfaces.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

/**
 * 分页响应DTO
 * 
 * <p>用于返回分页数据的HTTP响应对象</p>
 * 
 * @param <T> 数据类型
 * @author chenyunlong
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "分页响应")
public class PageResponse<T> {

    /**
     * 数据列表
     */
    @Schema(description = "数据列表")
    private List<T> content;

    /**
     * 当前页码
     */
    @Schema(description = "当前页码", example = "1")
    private Integer page;

    /**
     * 每页大小
     */
    @Schema(description = "每页大小", example = "20")
    private Integer size;

    /**
     * 总记录数
     */
    @Schema(description = "总记录数", example = "100")
    private Long total;

    /**
     * 总页数
     */
    @Schema(description = "总页数", example = "5")
    private Integer totalPages;

    /**
     * 是否为第一页
     */
    @Schema(description = "是否为第一页", example = "true")
    private Boolean first;

    /**
     * 是否为最后一页
     */
    @Schema(description = "是否为最后一页", example = "false")
    private Boolean last;

    /**
     * 是否有下一页
     */
    @Schema(description = "是否有下一页", example = "true")
    private Boolean hasNext;

    /**
     * 是否有上一页
     */
    @Schema(description = "是否有上一页", example = "false")
    private Boolean hasPrevious;

    /**
     * 当前页数据数量
     */
    @Schema(description = "当前页数据数量", example = "20")
    private Integer numberOfElements;

    /**
     * 是否为空页
     */
    @Schema(description = "是否为空页", example = "false")
    private Boolean empty;

    /**
     * 创建分页响应
     * 
     * @param content 数据列表
     * @param page 当前页码
     * @param size 每页大小
     * @param total 总记录数
     * @param <T> 数据类型
     * @return 分页响应
     */
    public static <T> PageResponse<T> of(List<T> content, Integer page, Integer size, Long total) {
        int totalPages = (int) Math.ceil((double) total / size);
        boolean first = page == 1;
        boolean last = page >= totalPages;
        boolean hasNext = page < totalPages;
        boolean hasPrevious = page > 1;
        int numberOfElements = content != null ? content.size() : 0;
        boolean empty = numberOfElements == 0;

        return PageResponse.<T>builder()
                .content(content)
                .page(page)
                .size(size)
                .total(total)
                .totalPages(totalPages)
                .first(first)
                .last(last)
                .hasNext(hasNext)
                .hasPrevious(hasPrevious)
                .numberOfElements(numberOfElements)
                .empty(empty)
                .build();
    }

    /**
     * 创建空分页响应
     * 
     * @param page 当前页码
     * @param size 每页大小
     * @param <T> 数据类型
     * @return 空分页响应
     */
    public static <T> PageResponse<T> empty(Integer page, Integer size) {
        return of(List.of(), page, size, 0L);
    }
}
