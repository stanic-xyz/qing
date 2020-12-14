package chenyunlong.zhangli.model.param;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Stan
 */
@Data
@EqualsAndHashCode
public class AnimeQuery implements Serializable {

    /**
     * 关键字
     */
    private String keyword;
    @NotBlank
    private String name;
    @NotBlank
    private String instruction;
    @NotBlank
    private String district;
    @NotBlank
    private String type;
    private String orignalName;
    private String otherName;
    private String author;
    private String company;
    private String premiereDate;
    private String playStatus;
    private String plotType;
    private String tags;
    private String officialWebsite;
}
