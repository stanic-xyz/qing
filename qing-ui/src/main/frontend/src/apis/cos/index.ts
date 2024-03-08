import { http } from "@/utils/service";
import type { Anime } from "@/apis/anime/types";
import type { QingPageResponse, QingResponse } from "@/utils/http/types";
import type { PageRequest } from "@/apis/auth/types";
import type { TempSecret } from "@/apis/cos/types";

const serviceUrl: string = "api/sys/file/";

/** 卡片列表 */
export const getAnimeList = (data?: object) => {
  console.log("请求参数", data);
  return http.request<QingResponse<QingPageResponse<Anime>>>("post", serviceUrl + "page", data);
};

/** 卡片列表 */
export const createTempSecret = () => {
  console.log("创建临时密钥");
  return http.request<QingResponse<TempSecret>>("get", serviceUrl + "createTempSecret");
};
