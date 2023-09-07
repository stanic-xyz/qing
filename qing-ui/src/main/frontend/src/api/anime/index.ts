import { http } from "@/utils/service";
import type { Anime } from "@/api/anime/types";
import type { QingPageResponse, QingResponse } from "@/utils/http/types";
import { useQuery } from "@vue/apollo-composable";
import gql from "graphql-tag";

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
  const { result } = useQuery(gql`
    query getUsers {
      artifactRepositories {
        id
        name
      }
    }
  `);

  return http.request<QingResponse<QingPageResponse<Anime>>>(
    "post",
    "/api/v1/anime/page",
    data
  );
};
