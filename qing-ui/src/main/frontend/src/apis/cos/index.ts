import {http} from "@/utils/service";
import type {Anime} from "@/apis/anime/types";
import type {QingPageResponse, QingResponse} from "@/utils/http/types";
import type {TempSecret} from "@/apis/cos/types";

const serviceUrl = "api/sys/file/";

/** 卡片列表 */
export const getAnimeList = (data?: object) => {
    return http.request<QingResponse<QingPageResponse<Anime>>>("post", serviceUrl + "page", data);
};

/** 卡片列表 */
export const createTempSecret = () => {
    return http.request<QingResponse<TempSecret>>("get", serviceUrl + "createTempSecret");
};
