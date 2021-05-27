package chenyunlong.zhangli.model.dto;

import chenyunlong.zhangli.model.dto.base.OutputConverter;
import chenyunlong.zhangli.model.entities.AnimeComment;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AnimeCommentDTO implements OutputConverter<AnimeCommentDTO, AnimeComment> {
    private Long id;
    private Long cid;
    private String username;
    private String content;
    private String ipAddress;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createTime;
}
