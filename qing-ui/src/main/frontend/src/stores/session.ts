import { defineStore } from "pinia";
import { logOut } from "@/api/auth";

export const userInfoStore = defineStore("userInfo", {
  state: () => {
    return {
      accessToken: "",
      username: "username",
      nickname: "Static",
      avatar: "https://dummyimage.com/100x100",
      expireAt: null,
      idToken: "id_token",
      tokenType: "Bearer",
    };
  },
  actions: {
    login() {
      console.log("登录功能");
    },
    isLoggedIn(): boolean {
      console.log(this.accessToken);
      console.log("判断用户是否登录", this.avatar);
      let result = false;
      if (typeof this.accessToken == "undefined" || this.accessToken) {
        result = false;
      }
      console.log("当前登录状态", result);
      return result;
    },
    logOut(): Promise<any> {
      console.log("退出登录");
      this.$reset();
      return logOut(this.accessToken);
    },
  },
});
