package chenyunlong.zhangli.model.params;

import chenyunlong.zhangli.model.entities.anime.AnimeInfo;
import chenyunlong.zhangli.model.dto.base.InputConverter;
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
        return LocalDate.now().plusYears(-100);
    }

    public LocalDate getEndYear() {
        return LocalDate.now();
    }
}
