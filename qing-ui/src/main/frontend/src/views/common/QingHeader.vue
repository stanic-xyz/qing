<style scoped lang="scss">
@import "../../assets/css/header.css";

.header-wrapper {
  width: 100%;
  box-sizing: border-box;
  background-color: #292929;
  display: flex;
  flex-direction: column;
  justify-content: center;
  justify-items: center;
  align-items: center;
  padding: 1rem 1rem 1rem 1rem;

  .header-container {
    width: 1140px;
    height: 100px;
    padding-left: 1rem;
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: space-between;

    .logo {
      display: block;
      padding: 0 1rem;
      height: 100px;

      a {
        font-size: 20px;
      }
    }
  }

  .nav {
    width: 1140px;
    max-width: 1140px;
    height: 56px;
    background-color: #292929;
    margin: auto;
    padding-left: 1rem;

    .nav_button {
      box-sizing: border-box;
      display: inline-block;
      height: 56px;
      line-height: 56px;
      padding: 0;
      margin: 0 10px;
      font-size: 1.2em;
      text-decoration: none;
      border-top-left-radius: 1px;
      border-top-right-radius: 2px;
      color: white;
    }

    .nav_button:hover,
    .nav_button:active {
      background-color: #303030;
      color: red;
    }

    .nav_button_current {
      background-color: #303030;
      color: red;
      position: relative;

      &:after {
        content: "";
        position: absolute;
        left: 50%;
        bottom: -1rem;
        transform: translateX(-50%);
        width: 100%;
        height: 2px;
        background-color: red;
      }
    }
  }
}
</style>

<template>
  <div class="header-wrapper">
    <div class="header-container">
      <div id="logo">
        <router-link to="/"><span style="display: inline-block; padding-left: 1rem">飞翔动漫</span></router-link>
      </div>
    </div>
    <div class="nav" style="position: relative; left: 0">
      <ul>
        <li v-for="(link, index) in data.links" v-bind:key="index">
          <router-link :class="{ nav_button_current: data.activeIndex === link.index }" class="nav_button" v-bind:to="link.path" @click="handleSelect(link.index)">{{ link.name }}</router-link>
        </li>
      </ul>
    </div>
  </div>
</template>

<script lang="ts" setup>
import {onMounted, reactive} from "vue";
import {userInfoStore} from "@/stores/session";
import {useRouter} from "vue-router";

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
