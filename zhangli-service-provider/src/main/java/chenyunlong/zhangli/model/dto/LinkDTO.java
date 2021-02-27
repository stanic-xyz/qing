package chenyunlong.zhangli.model.dto;

import chenyunlong.zhangli.entities.Link;
import chenyunlong.zhangli.model.dto.base.OutputConverter;
import lombok.Data;

/**
 * Link output dto.
 *
 * @author ryanwang
 * @date 2019/3/21
 */
@Data
public class LinkDTO implements OutputConverter<LinkDTO, Link> {

    private Integer id;

    private String name;

    private String url;

    private String logo;

    private String description;

    private String team;

    private Integer priority;
}
