package chenyunlong.zhangli.entities;

import chenyunlong.zhangli.entities.common.BaseEntitiy;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class UploadFile extends BaseEntitiy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long fileID;
    @Column
    private String fileName;
    @Column
    private String mimeType;
    @Column
    private String url;
    @Column
    private long fileSize;
}
