import { createApp } from "vue";
import { createPinia } from "pinia";

import App from "./App.vue";
import router from "./router";

import "./assets/css/common.css";

import LayUI, {
  LayBody,
  LayButton,
  LayCard,
  LayContainer,
  LayEmpty,
  LayException,
  LayFooter,
  LayForm,
  LayFormItem,
  LayHeader,
  LayIcon,
  LayInput,
  LayLayout,
  LayPageHeader,
  LaySide,
  LayTable,
} from "@layui/layui-vue";
import "@layui/layui-vue/lib/index.css";

const app = createApp(App);

app.use(createPinia());
app.use(router);
app.use(LayUI);

app.component("LayButton", LayButton);
app.component("LayTable", LayTable);
app.component("LayPageHeader", LayPageHeader);
app.component("LayIcon", LayIcon);
app.component("LayContainer", LayContainer);
app.component("LayFormItem", LayFormItem);
app.component("LayInput", LayInput);
app.component("LayForm", LayForm);
app.component("LayLayout", LayLayout);
app.component("LayHeader", LayHeader);
app.component("LayBody", LayBody);
app.component("LayFooter", LayFooter);
app.component("LaySide", LaySide);
app.component("LayCard", LayCard);
app.component("LayException", LayException);
app.component("LayEmpty", LayEmpty);

app.mount("#app");
