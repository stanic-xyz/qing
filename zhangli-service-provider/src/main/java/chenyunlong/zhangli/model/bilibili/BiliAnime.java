
package chenyunlong.zhangli.model.bilibili;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

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

    public static class Builder {

        private String badge;
        private BadgeInfo badgeInfo;
        private Long badgeType;
        private String cover;
        private String indexShow;
        private Long isFinish;
        private String link;
        private Long mediaId;
        private String order;
        private String orderType;
        private Long seasonId;
        private Long seasonType;
        private String title;
        private String titleIcon;

        public Builder withBadge(String badge) {
            this.badge = badge;
            return this;
        }

        public Builder withBadgeInfo(BadgeInfo badgeInfo) {
            this.badgeInfo = badgeInfo;
            return this;
        }

        public Builder withBadgeType(Long badgeType) {
            this.badgeType = badgeType;
            return this;
        }

        public Builder withCover(String cover) {
            this.cover = cover;
            return this;
        }

        public Builder withIndexShow(String indexShow) {
            this.indexShow = indexShow;
            return this;
        }

        public Builder withIsFinish(Long isFinish) {
            this.isFinish = isFinish;
            return this;
        }

        public Builder withLink(String link) {
            this.link = link;
            return this;
        }

        public Builder withMediaId(Long mediaId) {
            this.mediaId = mediaId;
            return this;
        }

        public Builder withOrder(String order) {
            this.order = order;
            return this;
        }

        public Builder withOrderType(String orderType) {
            this.orderType = orderType;
            return this;
        }

        public Builder withSeasonId(Long seasonId) {
            this.seasonId = seasonId;
            return this;
        }

        public Builder withSeasonType(Long seasonType) {
            this.seasonType = seasonType;
            return this;
        }

        public Builder withTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder withTitleIcon(String titleIcon) {
            this.titleIcon = titleIcon;
            return this;
        }

        public BiliAnime build() {
            BiliAnime list = new BiliAnime();
            list.badge = badge;
            list.badgeInfo = badgeInfo;
            list.badgeType = badgeType;
            list.cover = cover;
            list.indexShow = indexShow;
            list.isFinish = isFinish;
            list.link = link;
            list.mediaId = mediaId;
            list.order = order;
            list.orderType = orderType;
            list.seasonId = seasonId;
            list.seasonType = seasonType;
            list.title = title;
            list.titleIcon = titleIcon;
            return list;
        }

    }

}
