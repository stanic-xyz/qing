package cn.chenyunlong.zhangli.model.params;

import cn.chenyunlong.zhangli.model.dto.base.InputConverter;
import cn.chenyunlong.zhangli.model.entities.anime.AnimeInfo;
import cn.chenyunlong.zhangli.utils.StringUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * @author Stan
 */
@Data
@EqualsAndHashCode
public class AnimeInfoQuery implements InputConverter<AnimeInfo> {

    private String keyword = "";
    private String district = "all";
    private String name = "";
    private String version = "all";
    private String year = "all";
    private String status = "all";
    private String type = "all";
    private Integer seasonMonth = -1;
    private String resourceType = "all";
    private String sort = "premiere_date";

    public LocalDate getStartYear() {
        if (StringUtils.isNotBlank(getYear()) && !"all".equals(getYear())) {
            String[] strings = getYear().split(",");
            return LocalDate.of(Integer.parseInt(strings[0].replace("[", "")), 1, 1);
        }
        return LocalDate.now().plusYears(-100);
    }

    public LocalDate getEndYear() {
        if (StringUtils.isNotBlank(getYear()) && !"all".equals(getYear())) {
            String[] strings = getYear().split(",");
            return LocalDate.of(Integer.parseInt(strings[1].replace(")", "")), 12, 31);
        }
        return LocalDate.now();
    }
}
