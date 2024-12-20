import {http} from "@/utils/service";
import type {Tag} from "@/apis/tags/types";
import type {QingPageResponse, QingResponse} from "@/utils/http/types";
import type {PageRequest} from "@/apis/auth/types";

const serviceUrl = "api/v1/tag";

/** 卡片列表 */
export const getTagList = (data?: object) => {
    return http.request<QingResponse<QingPageResponse<Tag>>>("post", serviceUrl + "/page", data);
};

/** 卡片列表 */
export const createTag = (data?: Tag) => {
    return http.request<QingResponse<QingPageResponse<Tag>>>("post", serviceUrl, data);
};

/** 更新动漫标签信息 */
export const updateTag = (id: string, data?: Tag) => {
    return http.request<QingResponse<QingPageResponse<Tag>>>("post", serviceUrl + "/updateTag", data);
};

/** 更新动漫标签信息 */
export const valid = (id: string) => {
    return http.request<QingResponse<string>>("post", serviceUrl + "/valid/" + id);
};

/** 更新动漫标签信息 */
export const invalid = (id: string) => {
    return http.request<QingResponse<QingPageResponse<string>>>("post", serviceUrl + "/invalid/" + id);
};

/** 分页查询动漫标签信息 */
export const page = (data: PageRequest<Tag>) => {
    return http.request<QingResponse<QingPageResponse<Tag>>>("post", serviceUrl + "/page", data);
};
