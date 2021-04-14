
package chenyunlong.zhangli.model.bilibili;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BadgeInfo {

    @SerializedName("bg_color")
    private String bgColor;
    @SerializedName("bg_color_night")
    private String bgColorNight;
    @Expose
    private String text;

    public String getBgColor() {
        return bgColor;
    }

    public String getBgColorNight() {
        return bgColorNight;
    }

    public String getText() {
        return text;
    }

    public static class Builder {

        private String bgColor;
        private String bgColorNight;
        private String text;

        public Builder withBgColor(String bgColor) {
            this.bgColor = bgColor;
            return this;
        }

        public Builder withBgColorNight(String bgColorNight) {
            this.bgColorNight = bgColorNight;
            return this;
        }

        public Builder withText(String text) {
            this.text = text;
            return this;
        }

        public BadgeInfo build() {
            BadgeInfo badgeInfo = new BadgeInfo();
            badgeInfo.bgColor = bgColor;
            badgeInfo.bgColorNight = bgColorNight;
            badgeInfo.text = text;
            return badgeInfo;
        }

    }

}
