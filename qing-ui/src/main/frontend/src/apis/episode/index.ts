import {http} from "@/utils/service";
import type {QingPageResponse, QingResponse} from "@/utils/http/types";
import type {PageRequest} from "@/apis/auth/types";
import type {Episode} from "@/apis/episode/types";

const serviceUrl = "api/v1/Episode";

/** 卡片列表 */
export const getEpisodeList = (data?: object) => {
    console.log("请求参数", data);
    return http.request<QingResponse<QingPageResponse<Episode>>>("post", serviceUrl + "/page", data);
};

/** 卡片列表 */
export const createEpisode = (data?: Episode) => {
    console.log("请求参数", data);
    return http.request<QingResponse<QingPageResponse<Episode>>>("post", serviceUrl, data);
};

/** 更新动漫标签信息 */
export const updateEpisode = (id: string, data?: Episode) => {
    console.log("请求参数", data);
    return http.request<QingResponse<QingPageResponse<Episode>>>("post", serviceUrl + "/updateEpisode", data);
};

/** 分页查询动漫标签信息 */
export const page = (data: PageRequest<Episode>) => {
    console.log("禁用：请求参数:{}", JSON.stringify(data));
    return http.request<QingResponse<QingPageResponse<Episode>>>("post", serviceUrl + "/page", data);
};
