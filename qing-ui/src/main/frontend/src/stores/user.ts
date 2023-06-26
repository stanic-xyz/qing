import { defineStore } from "pinia";

export const userStore = defineStore({
  id: "user",
  state: () => {
    return {
      token: "",
      userInfo: {},
      permissions: [],
      menus: [],
    };
  },
  actions: {},
});
