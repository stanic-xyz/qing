import {defineStore} from "pinia";

export const userStore = defineStore({
    id: "user",
    state: () => {
        const token = localStorage.getItem("qing_token");
        return {
            token: token || "",
            userInfo: {},
            permissions: [],
            menus: [],
        };
    },
    actions: {},
});
