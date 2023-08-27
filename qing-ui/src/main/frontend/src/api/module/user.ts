import { http } from "@/utils/service";

export const login = function (loginForm: any) {
  return http.post("/user/login", loginForm);
};

export const menu = function () {
  return http.get("/user/menu");
};

export const permission = function () {
  let param;
  return http.get("/user/permission", param);
};

export const userInfo = function (params: any) {
  return http.get("user/getUserInfo");
};
