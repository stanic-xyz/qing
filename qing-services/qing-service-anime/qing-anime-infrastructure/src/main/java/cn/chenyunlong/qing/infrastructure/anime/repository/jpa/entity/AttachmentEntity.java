package cn.chenyunlong.qing.infrastructure.anime.repository.jpa.entity;

import cn.chenyunlong.common.annotation.FieldDesc;
import cn.chenyunlong.jpa.support.BaseJpaEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "anime_attachment")
public class AttachmentEntity extends BaseJpaEntity {

    @FieldDesc(name = "文件类型", description = "文件描述信息")
    private String mimeType;

    @FieldDesc(name = "文件原始名称")
    private String fileName;

    @FieldDesc(name = "文件大小")
    private Long fileSize;

    @FieldDesc(name = "文件地址", description = "文件上传地址")
    private String path;

    @FieldDesc(name = "存储路径", description = "存储路径（本地存储：绝对路径；云存储：对象URL或Key）")
    private String storagePath;

    @FieldDesc(name = "存储类型")
    private Long storageType;

    @FieldDesc(name = "文件内容哈希值", description = "文件内容哈希值（如MD5、SHA-256），用于检测重复文件")
    private String contentHash;

    @FieldDesc(name = "上传时间", description = "文件上传时间")
    private Instant uploadTime;


    public void delete() {

    }
}
