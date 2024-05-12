/*
 * Copyright (c) 2019-2023  YunLong Chen
 * Project Qing is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 *
 */

package cn.chenyunlong.qing.domain.third.bilibili.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BadgeInfo {

    @JsonProperty("bg_color")
    private String bgColor;
    @JsonProperty("bg_color_night")
    private String bgColorNight;
    private String text;

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
