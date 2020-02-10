package chenyunlong.zhangli.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
public class UploadFile implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileID;
    @Column
    private String fileName;
    @Column
    private String mimeType;
    @Column
    private String url;
    @Column
    private Long fileSize;
}
