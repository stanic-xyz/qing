package cn.chenyunlong.qing.service.llm.dto;

import lombok.Data;

import java.util.List;

@Data
public class CategoryTreeDTO {
    private Long id;
    private String name;
    private Long parentId;
    private Integer level;
    private String icon;
    private String type;
    private List<CategoryTreeDTO> children;
}
