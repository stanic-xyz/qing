import { qingHttp } from "@/utils/service";

/** 登录 */
export const getLogin = (username: string, password: string) => {
  return qingHttp.post("api/authorize/formLogin", {
    username: username,
    password: password,
  });
};

/** 卡片列表 */
export const getAnimeList = (data?: object) => {
  return qingHttp.get("/getCardList", {
    page: 1,
    pageSize: 2,
  });
};

export interface Anime {
  id: Number;
  animeId?: string;
  name?: string;
  coverUrl?: string;
  premiereDate?: string;
  districtId?: Number;
  instruction?: string;
  districtName?: string;
  typeId?: Number;
  typeName?: string;
  originalName?: string;
  otherName?: string;
  author?: string;
  company?: string;
  playStatus?: string;
  plotType?: string;
  tags?: Array<string>;
  officialWebsite?: string;
  playHeat?: string;
}
