package cn.chenyunlong.zhangli.model.agefans;

import lombok.Data;

import java.util.List;

@Data
public class AgeAnimeInfo {
    private List<AgePlayList> agePlayListList;
    private List<AgeRecommendInfo> recommendList;
    private AgeBaiduNet baiduNet;
    private Long animeId;
    private String name;
    private String district;
    private String instruction;
    private String coverUrl;
    private String detailCoverUrl;
    private String type;
    private String originalName;
    private String otherName;
    private String author;
    private String company;
    private String premiereDate;
    private String playStatus;
    private String plotType;
    private String tags;
    private String officialWebsite;
    private String playHeat;
}
