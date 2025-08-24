package cn.chenyunlong.qing.anime.domain.attachement;

import cn.chenyunlong.qing.domain.common.EntityId;
import lombok.Getter;
import lombok.ToString;

/**
 * 附件ID值对象
 *
 * @author 陈云龙
 */
@Getter
@ToString
public class AttachmentId extends EntityId<Long> {

    private final Long value;

    private AttachmentId(Long value) {
        if (value == null) {
            throw new IllegalArgumentException("AttachmentId value cannot be null");
        }
        this.value = value;
    }

    public static AttachmentId of(Long value) {
        return new AttachmentId(value);
    }

    public static AttachmentId of(String id) {
        return new AttachmentId(Long.valueOf(id));
    }

    public static AttachmentId generate() {
        return new AttachmentId(System.currentTimeMillis());
    }
}
