import {http} from "@/utils/service";
import type {Anime} from "@/apis/anime/types";
import type {QingPageResponse, QingResponse} from "@/utils/http/types";
import type {PageRequest} from "@/apis/auth/types";

const serviceUrl: string = "api/v1/anime/";

/** 卡片列表 */
export const getAnimeList = (data?: object) => {
  console.log("请求参数", data);
  return http.request<QingResponse<QingPageResponse<Anime>>>("post", serviceUrl + "page", data);
};

/** 卡片列表 */
export const createAnime = (data?: Anime) => {
  console.log("请求参数", data);
  return http.request<QingResponse<QingPageResponse<Anime>>>("post", serviceUrl + "page", data);
};

/** 更新动漫信息 */
export const updateAnime = (id: String, data?: Anime) => {
  console.log("请求参数", data);
  return http.request<QingResponse<QingPageResponse<Anime>>>("post", serviceUrl + "updateAnime", data);
};

/** 更新动漫信息 */
export const valid = (id: String) => {
  console.log("请求参数:{}", id);
  return http.request<QingResponse<String>>("post", serviceUrl + "valid/" + id);
};

/** 更新动漫信息 */
export const invalid = (id: String) => {
  console.log("禁用：请求参数:{}", id);
  return http.request<QingResponse<QingPageResponse<String>>>("post", serviceUrl + "invalid/" + id);
};

/** 更新动漫信息 */
export const findById = (id: String) => {
  console.log("禁用：请求参数:{}", id);
  return http.request<QingResponse<Anime>>("get", serviceUrl + "findById/" + id);
};

/** 分页查询动漫信息 */
export const page = (data: PageRequest<Anime>) => {
  console.log("禁用：请求参数:{}", JSON.stringify(data));
  return http.request<QingResponse<QingPageResponse<Anime>>>("post", serviceUrl + "page", data);
};
