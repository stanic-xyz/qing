package chenyunlong.zhangli.model.agefans;

import chenyunlong.zhangli.model.entities.AnimeMenu;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Stan
 */
@Data
public class CommModel implements Serializable {
    private String index;
    private String indexName;
    private String indexImg;
    private List<AnimeMenu> animeMenus;
    private String notice;
    private List<FriendLink> friendLinks;
}
