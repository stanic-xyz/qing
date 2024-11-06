import {http} from "@/utils/service";
import type {QingPageResponse, QingPageResponseDetail, QingResponse} from "@/utils/http/types";
import type {PageRequest} from "@/apis/auth/types";
import type {PlayList} from "@/apis/playList/types";

const serviceUrl = "api/v1/PlayList";

/** 卡片列表 */
export const getPlayListList = (data?: object) => {
    return http.request<QingResponse<QingPageResponseDetail<PlayList>>>("post", serviceUrl + "/page", data);
};

/** 卡片列表 */
export const createPlayList = (data?: PlayList) => {
    return http.request<QingResponse<QingPageResponse<PlayList>>>("post", serviceUrl, data);
};

/** 更新动漫信息 */
export const updatePlayList = (id: string, data?: PlayList) => {
    return http.request<QingResponse<QingPageResponse<PlayList>>>("post", serviceUrl + "/updatePlayList", data);
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
    return http.request<QingResponse<PlayList>>("get", serviceUrl + "/findById/" + id);
};

/** 更新动漫信息 */
export const findDetailById = (id: number) => {
    return http.request<QingResponse<PlayList>>("get", serviceUrl + "/findDetailById/" + id);
};

/** 分页查询动漫信息 */
export const page = (data: PageRequest<PlayList>) => {
    return http.request<QingResponse<QingPageResponseDetail<PlayList>>>("post", serviceUrl + "/page", data);
};
