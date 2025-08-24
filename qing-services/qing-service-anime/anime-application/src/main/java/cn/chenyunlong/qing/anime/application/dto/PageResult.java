package cn.chenyunlong.qing.anime.application.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Collections;
import java.util.List;

/**
 * 分页结果
 *
 * <p>用于封装分页查询结果的通用类</p>
 *
 * @param <T> 数据类型
 * @author chenyunlong
 * @since 1.0.0
 */
@Data
@Builder
public class PageResult<T> {

    /**
     * 数据列表
     */
    @Builder.Default
    private List<T> data = Collections.emptyList();

    /**
     * 总记录数
     */
    @Builder.Default
    private Long total = 0L;

    /**
     * 当前页码（从1开始）
     */
    @Builder.Default
    private Integer page = 1;

    /**
     * 每页大小
     */
    @Builder.Default
    private Integer size = 20;

    /**
     * 总页数
     */
    private Integer totalPages;

    /**
     * 是否有下一页
     */
    private Boolean hasNext;

    /**
     * 是否有上一页
     */
    private Boolean hasPrevious;

    /**
     * 是否为第一页
     */
    private Boolean isFirst;

    /**
     * 是否为最后一页
     */
    private Boolean isLast;

    /**
     * 当前页数据数量
     */
    private Integer numberOfElements;

    /**
     * 是否为空页
     */
    private Boolean empty;

    /**
     * 构建完整的分页结果
     *
     * @param data  数据列表
     * @param total 总记录数
     * @param page  当前页码
     * @param size  每页大小
     * @param <T>   数据类型
     * @return 分页结果
     */
    public static <T> PageResult<T> of(List<T> data, Long total, Integer page, Integer size) {
        PageResultBuilder<T> builder = PageResult.<T>builder()
                .data(data != null ? data : Collections.emptyList())
                .total(total != null ? total : 0L)
                .page(page != null ? page : 1)
                .size(size != null ? size : 20);

        return builder.build().calculateDerivedFields();
    }

    /**
     * 创建空的分页结果
     *
     * @param page 当前页码
     * @param size 每页大小
     * @param <T>  数据类型
     * @return 空的分页结果
     */
    public static <T> PageResult<T> empty(Integer page, Integer size) {
        return of(Collections.emptyList(), 0L, page, size);
    }

    /**
     * 创建单页结果
     *
     * @param data 数据列表
     * @param <T>  数据类型
     * @return 单页结果
     */
    public static <T> PageResult<T> single(List<T> data) {
        List<T> safeData = data != null ? data : Collections.emptyList();
        return of(safeData, (long) safeData.size(), 1, safeData.size());
    }

    /**
     * 计算派生字段
     *
     * @return 当前对象
     */
    private PageResult<T> calculateDerivedFields() {
        // 计算总页数
        this.totalPages = size > 0 ? (int) Math.ceil((double) total / size) : 0;

        // 计算是否有下一页
        this.hasNext = page < totalPages;

        // 计算是否有上一页
        this.hasPrevious = page > 1;

        // 计算是否为第一页
        this.isFirst = page == 1;

        // 计算是否为最后一页
        this.isLast = page >= totalPages;

        // 计算当前页数据数量
        this.numberOfElements = data.size();

        // 计算是否为空页
        this.empty = data.isEmpty();

        return this;
    }

    /**
     * 转换数据类型
     *
     * @param mapper 转换函数
     * @param <R>    目标数据类型
     * @return 转换后的分页结果
     */
    public <R> PageResult<R> map(java.util.function.Function<T, R> mapper) {
        List<R> mappedData = data.stream()
                .map(mapper)
                .collect(java.util.stream.Collectors.toList());

        return PageResult.<R>builder()
                .data(mappedData)
                .total(total)
                .page(page)
                .size(size)
                .totalPages(totalPages)
                .hasNext(hasNext)
                .hasPrevious(hasPrevious)
                .isFirst(isFirst)
                .isLast(isLast)
                .numberOfElements(mappedData.size())
                .empty(mappedData.isEmpty())
                .build();
    }

    /**
     * 获取开始索引（从0开始）
     *
     * @return 开始索引
     */
    public int getStartIndex() {
        return (page - 1) * size;
    }

    /**
     * 获取结束索引（从0开始，不包含）
     *
     * @return 结束索引
     */
    public int getEndIndex() {
        return Math.min(getStartIndex() + numberOfElements, total.intValue());
    }
}
