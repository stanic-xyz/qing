package cn.chenyunlong.qing.anime.domain.anime.models;

import java.util.List;

public record Tags(List<String> tags) {

    public boolean contains(String tag) {
        return tags.contains(tag);
    }

    // 创建tags
    public static Tags create(List<String> tagNameList) {
        return new Tags(tagNameList);
    }
}
