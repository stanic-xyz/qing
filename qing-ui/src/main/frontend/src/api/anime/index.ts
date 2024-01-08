import {http} from "@/utils/service";
import type {Anime} from "@/api/anime/types";
import type {QingPageResponse, QingResponse} from "@/utils/http/types";

/** 登录 */
export const getLogin = (username: string, password: string) => {
  return http.post("api/authorize/formLogin", {
    username: username,
    password: password,
  });
};

/** 卡片列表 */
export const getAnimeList = (data?: object) => {
  console.log("请求参数", data);

  return http.request<QingResponse<QingPageResponse<Anime>>>(
    "post",
    "/api/v1/anime/page",
    data,
  );
};
