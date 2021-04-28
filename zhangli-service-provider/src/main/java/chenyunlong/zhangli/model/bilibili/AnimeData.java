
package chenyunlong.zhangli.model.bilibili;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AnimeData {

    @JsonProperty("has_next")
    private Long hasNext;
    @Expose
    private List<BiliAnime> list;
    @Expose
    private Long num;
    @Expose
    private Long size;
    @Expose
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
