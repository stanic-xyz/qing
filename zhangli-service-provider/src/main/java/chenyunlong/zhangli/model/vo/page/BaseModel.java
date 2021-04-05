package chenyunlong.zhangli.model.vo.page;

import chenyunlong.zhangli.model.dto.LinkDTO;
import chenyunlong.zhangli.model.support.Pagination;
import lombok.Data;

import java.util.List;

@Data
public class BaseModel {

    private String hostname = "";

    private List<LinkDTO> links;

    private Pagination pagination;
}
