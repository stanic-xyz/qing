/**
 * Copyright 2020 json.cn
 */
package cn.chenyunlong.zhangli.model.agefans;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author Stan
 */
@Data
public class AgePlayInfoModel {

    private String purl;
    private String purlf;
    private String vurl;
    private String playid;
    @JsonProperty("vurl_bak")
    private String vurlBak;
    @JsonProperty("purl_mp4")
    private String purlMp4;
    private String ex;
}