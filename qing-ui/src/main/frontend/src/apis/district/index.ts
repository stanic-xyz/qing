import {http} from "@/utils/service";
import type {District} from "@/apis/district/types";
import type {QingPageResponse, QingResponse} from "@/utils/http/types";
import type {PageRequest} from "@/apis/auth/types";

const serviceUrl = "api/v1/district";

/** 卡片列表 */
export const getDistrictList = (data?: object) => {
    console.log("请求参数", data);
    return http.request<QingResponse<QingPageResponse<District>>>("post", serviceUrl + "/page", data);
};

/** 卡片列表 */
export const createDistrict = (data?: District) => {
    console.log("请求参数", data);
    return http.request<QingResponse<QingPageResponse<District>>>("post", serviceUrl, data);
};

/** 更新动漫标签信息 */
export const updateDistrict = (id: string, data?: District) => {
    console.log("请求参数", data);
    return http.request<QingResponse<QingPageResponse<District>>>("post", serviceUrl + "/updateDistrict", data);
};

/** 更新动漫标签信息 */
export const valid = (id: string) => {
    console.log("请求参数:{}", id);
    return http.request<QingResponse<string>>("post", serviceUrl + "/valid/" + id);
};

/** 更新动漫标签信息 */
export const invalid = (id: string) => {
    console.log("禁用：请求参数:{}", id);
    return http.request<QingResponse<QingPageResponse<string>>>("post", serviceUrl + "/invalid/" + id);
};

/** 分页查询动漫标签信息 */
export const page = (data: PageRequest<District>) => {
    console.log("禁用：请求参数:{}", JSON.stringify(data));
    return http.request<QingResponse<QingPageResponse<District>>>("post", serviceUrl + "/page", data);
};
