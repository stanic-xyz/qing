package cn.chenyunlong.qing.anime.domain.anime.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum District {


    CHINA(1, "国漫"),

    JAPAN(2, "日漫"),

    US(3, "美漫"),

    OTHER(-1, "其他");

    @JsonValue
    private final Integer value;
    private final String name;

    District(Integer value, String name) {
        this.value = value;
        this.name = name;
    }
}
