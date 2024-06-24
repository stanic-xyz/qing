import { http } from "@/utils/service";
import type { Category } from "@/apis/categories/types";
import type { QingPageResponse, QingResponse } from "@/utils/http/types";
import type { PageRequest } from "@/apis/auth/types";

const serviceUrl: string = "/api/v1/anime-category";

/** 卡片列表 */
export const getCategoryList = (data?: object) => {
  console.log("请求参数", data);
  return http.request<QingResponse<QingPageResponse<Category>>>("post", serviceUrl + "/page", data);
};

/** 卡片列表 */
export const createCategory = (data?: Category) => {
  console.log("请求参数", data);
  return http.request<QingResponse<QingPageResponse<Category>>>("post", serviceUrl, data);
};

/** 更新动漫标签信息 */
export const updateCategory = (id: String, data?: Category) => {
  console.log("请求参数", data);
  return http.request<QingResponse<QingPageResponse<Category>>>("post", serviceUrl + "/updateCategory", data);
};

/** 更新动漫标签信息 */
export const valid = (id: String) => {
  console.log("请求参数:{}", id);
  return http.request<QingResponse<String>>("post", serviceUrl + "/valid/" + id);
};

/** 更新动漫标签信息 */
export const invalid = (id: String) => {
  console.log("禁用：请求参数:{}", id);
  return http.request<QingResponse<QingPageResponse<String>>>("post", serviceUrl + "/invalid/" + id);
};

/** 分页查询动漫标签信息 */
export const page = (data: PageRequest<Category>) => {
  console.log("禁用：请求参数:{}", JSON.stringify(data));
  return http.request<QingResponse<QingPageResponse<Category>>>("post", serviceUrl + "/page", data);
};
