
package cn.chenyunlong.zhangli.model.bilibili;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;
import lombok.Data;

@Data
public class BiliAnime {

    @Expose
    private String badge;
    @JsonProperty("badge_info")
    private BadgeInfo badgeInfo;
    @JsonProperty("badge_type")
    private Long badgeType;
    @Expose
    private String cover;
    @JsonProperty("index_show")
    private String indexShow;
    @JsonProperty("is_finish")
    private Long isFinish;
    @Expose
    private String link;
    @JsonProperty("media_id")
    private Long mediaId;
    @Expose
    private String order;
    @JsonProperty("order_type")
    private String orderType;
    @JsonProperty("season_id")
    private Long seasonId;
    @JsonProperty("season_type")
    private Long seasonType;
    @Expose
    private String title;
    @JsonProperty("title_icon")
    private String titleIcon;
}
