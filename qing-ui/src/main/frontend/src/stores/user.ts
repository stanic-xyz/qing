import {defineStore} from "pinia";

export const userStore = defineStore({
  id: "user",
  state: () => {
    let token = localStorage.getItem("qing_token");
    console.log("用户读取到配置信息", token);
    return {
      token: token || "",
      userInfo: {},
      permissions: [],
      menus: [],
    };
  },
  actions: {},
});
