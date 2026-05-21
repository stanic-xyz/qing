package cn.chenyunlong.qing.service.llm.dto.tag;

import lombok.Data;

@Data
public class CreateTagRequest {
    private String name;
    private String color;
}
