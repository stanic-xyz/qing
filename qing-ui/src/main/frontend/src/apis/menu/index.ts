import {http} from "@/utils/service";
import type {MenuItem} from "@/apis/menu/types";
import type {QingPageResponse, QingResponse} from "@/utils/http/types";
import type {PageRequest} from "@/apis/auth/types";

const serviceUrl = "api/v1/sys-menu";

/** 卡片列表 */
export const getMenuList = (data?: object) => {
    console.log("请求参数", data);
    return http.request<QingResponse<QingPageResponse<MenuItem>>>("post", serviceUrl + "/page", data);
};

/** 卡片列表 */
export const createMenu = (data?: MenuItem) => {
    console.log("请求参数", data);
    return http.request<QingResponse<QingPageResponse<MenuItem>>>("post", serviceUrl, data);
};

/** 更新动漫标签信息 */
export const updateMenu = (id: string, data?: MenuItem) => {
    console.log("请求参数", data);
    return http.request<QingResponse<QingPageResponse<MenuItem>>>("post", serviceUrl + "/updateMenu", data);
};

/** 更新动漫标签信息 */
export const invalid = (id: string) => {
    console.log("禁用：请求参数:{}", id);
    return http.request<QingResponse<QingPageResponse<string>>>("post", serviceUrl + "/invalid/" + id);
};

/** 分页查询动漫标签信息 */
export const page = (data: PageRequest<MenuItem>) => {
    console.log("禁用：请求参数:{}", JSON.stringify(data));
    return http.request<QingResponse<QingPageResponse<MenuItem>>>("post", serviceUrl + "/page", data);
};

export const menuTree = () => {
    console.debug("禁用：请求菜单");
    return http.request<QingResponse<MenuItem[]>>("get", serviceUrl + "/menuTree");
};
