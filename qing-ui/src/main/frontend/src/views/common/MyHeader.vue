<script lang="ts" setup>
import { onMounted, reactive } from "vue";
import Notification from "@/views/common/Notification.vue";
import { userInfoStore } from "@/stores/session";

const data = reactive({
  time: new Date(),
  activeIndex: 1,
  links: [
    {
      name: "首页",
      index: 1,
      path: "/index",
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
  console.log(data);
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

  console.log(userInfoSto.accessToken);
  // router.push("/login");
}
</script>

<template>
  <div id="top">
    <div id="logo">
      <router-link to="/index">AGE动漫</router-link>
    </div>
    <div class="loginOut">
      <span class="svg_title svg_title_user"></span>
      <button id="comment_user" class="loginOut_btn2" @click="handleLogin">
        登陆
      </button>
      <button id="comment_user" class="loginOut_btn2" @click="handleLogout">
        退出
      </button>
    </div>
  </div>
  <div id="nav">
    <router-link
      v-for="(link, index) in data.links"
      v-bind:key="index"
      :class="{ nav_button_current: data.activeIndex === link.index }"
      class="nav_button"
      v-bind:to="link.path"
      @click="handleSelect(link.index)"
      >{{ link.name }}
    </router-link>
  </div>
  <Notification url="https://www.chenyunlong.cn"></Notification>
</template>
<style scoped src="../../assets/css/common/header.css"></style>
