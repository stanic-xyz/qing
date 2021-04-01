package chenyunlong.zhangli.model.vo;

import chenyunlong.zhangli.model.entities.AnimeType;
import chenyunlong.zhangli.model.entities.District;
import chenyunlong.zhangli.model.entities.Version;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Stan
 */
@Data
public class AnimeOptionsModel implements Serializable {
    private List<AnimeType> typeList;
    private List<Version> versionList;
    private List<District> districtList;
    private List<String> indexList;
    private List<YearInfo> years;
}
