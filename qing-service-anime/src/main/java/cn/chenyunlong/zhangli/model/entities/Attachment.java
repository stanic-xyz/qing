package cn.chenyunlong.zhangli.model.entities;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.io.Serializable;

/**
 * @author Stan
 * @date 2021/05/22
 */
@Data
@TableName("attachment")
public class Attachment {
    private Integer attachmentId;
    private String attachmentName;
    private String attachmentUrl;
}
