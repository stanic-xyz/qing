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
import java.util.List;
import lombok.Getter;

@Getter
public class AnimeData {

    @JsonProperty("has_next")
    private Long hasNext;

    private List<BiliAnime> list;

    private Long num;

    private Long size;

    private Long total;

    public Long getHasNext() {
        return hasNext;
    }

    public List<BiliAnime> getList() {
        return list;
    }

    public Long getNum() {
        return num;
    }

    public Long getSize() {
        return size;
    }

    public Long getTotal() {
        return total;
    }

    public static class Builder {

        private Long hasNext;
        private java.util.List<BiliAnime> list;
        private Long num;
        private Long size;
        private Long total;

        public Builder withHasNext(Long hasNext) {
            this.hasNext = hasNext;
            return this;
        }

        public Builder withList(java.util.List<BiliAnime> list) {
            this.list = list;
            return this;
        }

        public Builder withNum(Long num) {
            this.num = num;
            return this;
        }

        public Builder withSize(Long size) {
            this.size = size;
            return this;
        }

        public Builder withTotal(Long total) {
            this.total = total;
            return this;
        }

        public AnimeData build() {
            AnimeData data = new AnimeData();
            data.hasNext = hasNext;
            data.list = list;
            data.num = num;
            data.size = size;
            data.total = total;
            return data;
        }

    }

}
