package chenyunlong.zhangli.model.vo.page;

import chenyunlong.zhangli.core.Pagination;
import chenyunlong.zhangli.model.dto.LinkDTO;
import lombok.Data;

import java.util.List;

@Data
public class BaseModel {

    private String hostname = "";

    private List<LinkDTO> links;

    private Pagination pagination;
}
