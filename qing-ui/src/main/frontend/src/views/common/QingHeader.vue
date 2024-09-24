<template>
  <div class="qing-header">
    <div class="qing-header-wrapper x-container">
      <div class="qing-header-logo">
        <img src="../../assets/img/logo.png" alt="qing" height="38px" />
      </div>
      <div class="qing-header-content">
        <div class="qing-header-tab active" v-for="(link, index) in data.links" :key="index" @click="handleSelect(link)">{{ link.name }}</div>
      </div>
      <div class="qing-header-search">
        <input name="query" id="query" type="search" class="qing-header-search-input" placeholder="输入番名搜索" maxlength="8" value="" />
        <button class="qing-header-search-icon">
          <lay-icon size="24px" type="layui-icon-search"></lay-icon>
        </button>
      </div>
      <div class="qing-header-operation">
        <button class="qing-header-operation-history">
          <lay-icon size="24px" type="layui-icon-time"></lay-icon>
        </button>
        <lay-dropdown trigger="hover" updateAtScroll>
          <button class="qing-header-operation-login">用户中心</button>
          <template #content>
            <lay-dropdown-menu>
              <lay-dropdown-menu-item>选项一</lay-dropdown-menu-item>
              <lay-dropdown-menu-item>选项二</lay-dropdown-menu-item>
              <lay-dropdown-menu-item>选项三</lay-dropdown-menu-item>
            </lay-dropdown-menu>
          </template>
        </lay-dropdown>
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.qing-header {
  position: sticky;
  width: 100%;
  background-color: #212529;
  padding: 16px;

  .qing-header-wrapper {
    display: flex;
    justify-content: space-between;
    flex-direction: row;
    height: 40px;
    align-items: center;

    .qing-header-logo {
      display: block;
    }

    .qing-header-content {
      background: #212529;
      display: flex;
      justify-content: space-around;
      align-items: center;
      align-content: space-between;
      margin-left: 1rem;
      margin-right: auto !important;

      .qing-header-logo {
        height: 38px;
      }

      .qing-header-tab {
        color: #e8e8e8;
        font-size: 16px;
        margin-right: 1rem;
        margin-left: 1rem !important;
        cursor: pointer;
        display: inline-block;

        ::after {
          background-color: gray;
        }

        &:hover {
          color: red;
        }
      }
    }

    .qing-header-search {
      height: 36px;
      display: flex;
      margin-right: 1rem !important;
      border-radius: 5px;

      .qing-header-search-input {
        padding: 0 1rem;
        color: var(--bs-body-color);
      }

      .qing-header-search-icon {
        height: 36px;
        width: 36px;
      }
    }

    .qing-header-operation {
      display: flex;
      justify-content: center;

      .qing-header-operation-history {
        color: white;
        background-color: #dc3545;
        border-color: #dc3545;
        height: 36px;
        width: 36px;
        font-size: 16px;
        margin-right: 2px;
        border-radius: 5px;
      }

      .qing-header-operation-login {
        color: white;
        height: 36px;
        border-radius: 5px;
        padding: 0 1rem;
        background-color: #dc3545;
        border-color: #dc3545;
      }
    }
  }
}
</style>

<script lang="ts" setup>
import { onMounted, reactive } from "vue";
import { userInfoStore } from "@/stores/session";
import { useRouter } from "vue-router";

const router = useRouter();
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
  console.log("Header加载成功！");
});

function handleSelect(event: any) {
  router.push({ path: event.path });
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
