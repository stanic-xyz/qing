package cn.chenyunlong.zhangli.api;


import lombok.Data;

import java.util.List;

/**
 * 分页数据封装类
 *
 * @author Stan
 * @date 2021/01/22
 */
@Data
public class CommonPage<T> {
    private Integer pageNum;
    private Integer pageSize;
    private Integer totalPage;
    private Long total;
    private List<T> list;
}

