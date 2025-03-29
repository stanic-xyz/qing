package cn.chenyunlong.qing.domain.anime.anime.models;

import java.time.LocalDate;

public record PremiereDate(LocalDate date) {

    /**
     * 是否在指定日期之后
     */
    public boolean isAfter(PremiereDate other) {
        return this.date.isAfter(other.date());
    }
}
