<template>
  <div class="header-wrapper">
    <div class="brand-wrapper base-container">
      <div id="logo">
        <router-link to="/">
          <img src="../../assets/img/logo.png" alt="Logo" />
        </router-link>
      </div>
      <div class="user-wrapper">
        <LayDropdown trigger="hover" updateAtScroll>
          <span class="svg_title svg_title_user"><lay-avatar :src="src" radius /></span>
          <template #content>
            <LayDropdownMenu>
              <LaySpace direction="vertical">
                <lay-dropdown-menu-item class="item-btn">
                  <lay-button v-if="false" @click="handleLogout">登出</lay-button>
                </lay-dropdown-menu-item>
                <lay-dropdown-menu-item class="item-btn">
                  <lay-button border="blue" @click="handleLogin">登陆</lay-button>
                </lay-dropdown-menu-item>
              </LaySpace>
            </LayDropdownMenu>
          </template>
        </LayDropdown>
      </div>
    </div>
    <div class="nav base-container">
      <ul>
        <li>
          <router-link v-for="(link, index) in data.links" v-bind:key="index" :class="{ nav_button_current: data.activeIndex === link.index }" class="nav_button" v-bind:to="link.path" @click="handleSelect(link.index)">{{ link.name }} </router-link>
        </li>
      </ul>
    </div>
    <!--    <Notification class="base-container" url="https://www.chenyunlong.cn"></Notification>-->
  </div>
</template>

<script lang="ts" setup>
import { onMounted, reactive } from "vue";
import Notification from "@/views/common/Notification.vue";
import { userInfoStore } from "@/stores/session";
import { LayAvatar, LayButton } from "@layui/layui-vue";
import { useRouter } from "vue-router";

const router = useRouter();
const src = "https://files.authing.co/user-contents/photos/38c766c6-3e98-4321-a171-b95e60e379b8.png";
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

function handleLogin() {
  location.href = "http://localhost:8080/api/authorize/auth/login";
}

function handleLogout() {
  const userInfoSto = userInfoStore();
  userInfoSto.logOut().then((response) => {
    console.log("退出登录成功", response);
  });

  console.log(userInfoSto.token);
  router.push("/login");
}
</script>

<style scoped src="../../assets/css/header.css"></style>
