package cn.chenyunlong.zhangli.model.agefans;

import cn.chenyunlong.zhangli.model.entities.AnimeMenu;
import lombok.Data;

import java.util.List;

/**
 * @author Stan
 */
@Data
public class CommModel {
    private String index;
    private String indexName;
    private String indexImg;
    private List<AnimeMenu> animeMenus;
    private String notice;
    private List<FriendLink> friendLinks;
}
