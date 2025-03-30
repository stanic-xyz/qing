package cn.chenyunlong.qing.anime.domain.anime.models;

import java.util.List;

public record PlotTypes(List<String> plotTypes) {

    public boolean contains(String plotType) {
        return plotTypes.contains(plotType);
    }
}
