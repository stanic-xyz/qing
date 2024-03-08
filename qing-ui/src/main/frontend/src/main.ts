import { createApp } from "vue";
import { createPinia } from "pinia";

import App from "./App.vue";
import router from "./router";

import "./assets/css/common.css";

import LayUI, { LayButton, LayForm, LayUpload } from "@layui/layui-vue";
import "@layui/layui-vue/lib/index.css";

var app = createApp(App).use(createPinia()).use(router).use(LayUI);
// app.use(
//   createGuard({
//     appId: "6432d5c9e0502f0bb45319bf",
//     redirectUri: "http://localhost:8080",
//   })
// )
app.component("layUpload", LayUpload);
app.component("LayButton", LayButton);
app.component("LayForm", LayForm);
app.mount("#app");
