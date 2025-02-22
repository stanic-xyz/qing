import {http} from "@/utils/service";
import type {Anime, AnimeDetail} from "@/apis/anime/types";
import type {QingPageResponse, QingPageResponseDetail, QingResponse} from "@/utils/http/types";
import type {PageRequest} from "@/apis/auth/types";

const serviceUrl = "api/v1/anime";

/** 卡片列表 */
export const getAnimeList = (data?: object) => {
    return http.request<QingResponse<QingPageResponseDetail<Anime>>>("post", serviceUrl + "/page", data);
};

/** 卡片列表 */
export const createAnime = (data?: Anime) => {
    return http.request<QingResponse<QingPageResponse<Anime>>>("post", serviceUrl, data);
};

/** 更新动漫信息 */
export const updateAnime = (id: string, data?: Anime) => {
    return http.request<QingResponse<QingPageResponse<Anime>>>("post", serviceUrl + "/updateAnime", data);
};

/** 启用动漫信息 */
export const valid = (id: string) => {
    return http.request<QingResponse<string>>("post", serviceUrl + "/valid/" + id);
};

/** 更新动漫信息 */
export const invalid = (id: string) => {
    return http.request<QingResponse<QingPageResponse<string>>>("post", serviceUrl + "/invalid/" + id);
};

/** 更新动漫信息 */
export const findById = (id: number) => {
    return http.request<QingResponse<Anime>>("get", serviceUrl + "/findById/" + id);
};

/** 更新动漫信息 */
export const findDetailById = (id: number) => {
    return http.request<QingResponse<AnimeDetail>>("get", serviceUrl + "/findDetailById/" + id);
};

/** 分页查询动漫信息 */
export const page = (data: PageRequest<Anime>) => {
    return http.request<QingResponse<QingPageResponseDetail<Anime>>>("post", serviceUrl + "/page", data);
};
