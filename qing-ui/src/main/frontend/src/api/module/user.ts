import { qingHttp } from "@/utils/service";

export const login = function (loginForm: any) {
  return qingHttp.post("/user/login", loginForm);
};

export const menu = function () {
  return qingHttp.get("/user/menu");
};

export const permission = function () {
  let param;
  return qingHttp.get("/user/permission", param);
};

export const userInfo = function (params: any) {
  return qingHttp.get("user/getUserInfo");
};
