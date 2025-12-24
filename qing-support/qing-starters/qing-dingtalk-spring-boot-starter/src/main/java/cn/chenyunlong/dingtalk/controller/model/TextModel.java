package cn.chenyunlong.dingtalk.controller.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 文本模型。
 *
 * @author 文本模型。
 * @since 2022/10/2022/10/18
 */
@Getter
@Setter
@ToString
public class TextModel {
    private String openConversationId;
    private String txt;
}
