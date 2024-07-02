<script lang="ts" setup>
import SideBar from "@/views/layouts/sideBar/SideBar.vue";
import { onMounted } from "vue";

onMounted(() => {
  console.log("onMounted");
});
</script>

<template>
  <lay-layout class="dashboard">
    <lay-header> 表头</lay-header>
    <lay-layout>
      <lay-side>
        <side-bar />
      </lay-side>
      <lay-body>
        <router-view></router-view>
      </lay-body>
    </lay-layout>
  </lay-layout>
</template>

<style scoped lang="scss">
.dashboard {
  .layui-header {
    line-height: 60px;
    text-align: center;
    background: #5fb878;
    color: white;
    height: 100px;
  }

  .dashboard .layui-side {
    display: flex;
    background: #5fb878;
    align-items: center;
    justify-content: center;
    color: white;
    height: 100%;
  }

  .layui-body {
    display: flex;
    align-items: center;
    justify-content: center;
    color: white;
    height: calc(100vh - 100px);
  }

  .layui-footer {
    align-items: center;
    justify-content: center;
  }
}
</style>
