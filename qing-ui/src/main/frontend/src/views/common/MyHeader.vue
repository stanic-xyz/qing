<script lang="ts" setup>
import {onMounted, reactive} from "vue";
import Notification from "@/views/common/Notification.vue";
import {userInfoStore} from "@/stores/session";
import {LayAvatar, LayButton} from "@layui/layui-vue";

const src = "https://foruda.gitee.com/avatar/1677022544584087390/4835367_jmysy_1578975358.png";
const data = reactive({
  time: new Date(),
  activeIndex: 1,
  links: [
    {
      name: "首页",
      index: 1,
      path: "/",
    },
    {
      name: "每日推荐",
      index: 2,
      path: "/recommend",
    },
    {
      name: "最近更新",
      index: 3,
      path: "/update",
    },
    {
      name: "目录",
      index: 4,
      path: "/catalog",
    },
    {
      name: "排行榜",
      index: 5,
      path: "/rank",
    },
  ],
});

onMounted(() => {
  console.log("Header加载完毕！");
});

function handleSelect(event: any) {
  data.activeIndex = event;
}

function handleLogin(event: any) {
  location.href = "http://localhost:8080/api/authorize/auth/login";
}

function handleLogout() {
  const userInfoSto = userInfoStore();
  userInfoSto.logOut().then((response) => {
    console.log("退出登录成功", response);
  });

  console.log(userInfoSto.token);
  // router.push("/login");
}
</script>

<template>
  <div id="top">
    <div id="logo">
      <router-link to="/">AGE动漫</router-link>
    </div>
    <div class="loginOut">
      <lay-dropdown trigger="hover" updateAtScroll>
        <span class="svg_title svg_title_user"><lay-avatar :src="src" radius/></span>
        <template #content>
          <lay-dropdown-menu>
            <lay-space direction="vertical">
              <lay-dropdown-menu-item class="item-btn">
                <lay-button v-if="false" @click="handleLogout">登出</lay-button>
              </lay-dropdown-menu-item>
              <lay-dropdown-menu-item class="item-btn">
                <lay-button border="blue" @click="handleLogin">登陆</lay-button>
              </lay-dropdown-menu-item>
            </lay-space>
          </lay-dropdown-menu>
        </template>
      </lay-dropdown>
    </div>
  </div>
  <div id="nav">
    <router-link v-for="(link, index) in data.links" v-bind:key="index" :class="{ nav_button_current: data.activeIndex === link.index }" class="nav_button" v-bind:to="link.path" @click="handleSelect(link.index)">{{ link.name }}</router-link>
  </div>
  <Notification url="https://www.chenyunlong.cn"></Notification>
</template>
<style scoped src="../../assets/css/common/header.css">
.item-btn,
.item-btn:active {
  background-color: black;
}
</style>
