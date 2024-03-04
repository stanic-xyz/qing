import {defineStore} from "pinia";
import {logOut} from "@/apis/auth";
import type {LocationQueryValue} from "vue-router";

export const userInfoStore = defineStore("userInfo", {
  state: () => {
    let token = localStorage.getItem("qing_token");
    console.log("读取到配置信息", token);
    return {
      token: token || "",
      username: "未知用户",
      nickname: "位置用户",
      avatar: "https://dummyimage.com/100x100",
      expireAt: null,
      idToken: "id_token",
      tokenType: "Bearer",
      userInfo: {},
      permissions: [],
      menus: [],
    };
  },
  getters: {
    tokenHeader: (state) => state.tokenType + " " + state.token,
  },
  actions: {
    login(token: string | LocationQueryValue[]) {
      this.token = token.toString();
      localStorage.setItem("qing_token", this.token);
      console.log("登录功能:保存Token信息");
    },
    isLoggedIn(): boolean {
      console.log(this.token);
      console.log("判断用户是否登录", this.avatar);
      let result = false;
      if (typeof this.token == "undefined" || this.token) {
        result = false;
      }
      console.log("当前登录状态", result);
      return result;
    },
    logOut(): Promise<any> {
      console.log("退出登录");
      this.$reset();
      return logOut(this.token);
    },
  },
});
