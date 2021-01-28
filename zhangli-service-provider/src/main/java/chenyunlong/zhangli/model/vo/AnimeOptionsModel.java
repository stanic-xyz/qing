package chenyunlong.zhangli.model.vo;

import chenyunlong.zhangli.entities.AnimeType;
import chenyunlong.zhangli.entities.District;
import chenyunlong.zhangli.entities.Version;
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
}
