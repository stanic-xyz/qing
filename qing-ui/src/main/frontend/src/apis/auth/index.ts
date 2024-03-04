import {http} from "@/utils/service";
import type {RefreshTokenResult, UserResult} from "@/apis/auth/types";

/** 登录 */
export const formLogin = (username: string, password: string) => {
  return http.post("api/login", {
    username: username,
    password: password,
  });
};

/** 登录 */
export const login = (username: string, password: string) => {
  return http.post("api/authorize/formLogin", {
    username: username,
    password: password,
  });
};

export const exchangeToken = (code: string, state: string) => {
  return http.get("api/auth2/authorization", {
    code: code,
    state: state,
  });
};

export const logOut = (accessToken: string) => {
  return http.get("api/authorize/auth/logout", {
    token: accessToken,
  });
};
/** 登录 */
export const getLogin = (data?: object) => {
  return http.request<UserResult>("post", "/login", { data });
};

/** 刷新token */
export const refreshToken = (data?: any) => {
  return http.request<RefreshTokenResult>("post", "api/refreshToken", { data });
};
