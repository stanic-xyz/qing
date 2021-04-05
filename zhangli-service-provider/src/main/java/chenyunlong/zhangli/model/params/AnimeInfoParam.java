package chenyunlong.zhangli.model.params;

import chenyunlong.zhangli.model.dto.base.InputConverter;
import chenyunlong.zhangli.model.entities.anime.AnimeInfo;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
public class AnimeInfoParam implements InputConverter<AnimeInfo> {

    @NotBlank(message = "动漫名称不能为空")
    @Size(max = 255, message = "文章标题的字符长度不能超过 {max}")
    private String name;
    private String instruction;
    private String district;
    private String coverUrl;
    private String type;
    @NotBlank(message = "原始名称不能为空")
    private String originalName;
    private String otherName;
    @NotBlank(message = "作者不能为空")
    private String author;
    @NotBlank(message = "公司名称不能为空")
    private String company;
    @NotEmpty(message = "出版日期不能为空")
    private Date premiereDate;
    @NotEmpty(message = "播放状态不能为空")
    private String playStatus;
    private String plotType;
    private String tags;
    private String officialWebsite;
}
