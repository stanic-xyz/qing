package cn.chenyunlong.qing.anime.domain.anime.models;

import cn.hutool.core.collection.CollUtil;

import java.util.List;

public record PlotTypes(List<String> plotTypes) {

    public static PlotTypes empty() {
        return new PlotTypes(CollUtil.toList());
    }

    public boolean contains(String plotType) {
        return plotTypes.contains(plotType);
    }
}
