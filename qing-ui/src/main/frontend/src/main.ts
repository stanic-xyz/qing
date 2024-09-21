import {createApp} from "vue";
import {createPinia} from "pinia";

import App from "./App.vue";
import router from "./router";

import LayUI from "@layui/layui-vue";

const app = createApp(App);
app.use(createPinia());
app.use(router);
app.use(LayUI);
app.mount("#app");
