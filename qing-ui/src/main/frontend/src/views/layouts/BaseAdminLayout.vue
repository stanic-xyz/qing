<template>
  <lay-layout>
    <lay-side>
      <side-bar/>
    </lay-side>
    <lay-body>
      <lay-header>
        <lay-breadcrumb separator="-">
          <lay-breadcrumb-item>今天</lay-breadcrumb-item>
          <lay-breadcrumb-item>有些</lay-breadcrumb-item>
          <lay-breadcrumb-item>不开心</lay-breadcrumb-item>
        </lay-breadcrumb>
      </lay-header>
      <router-view></router-view>
    </lay-body>
  </lay-layout>
</template>

<script lang="ts" setup>
import SideBar from "@/components/sideBar/SideBar.vue";
import {onMounted} from "vue";

onMounted(() => {
  console.log("管理员首页加载成功");
});
</script>

<style lang="scss" scoped>
//.dashboard {
//  .layui-header {
//    line-height: 60px;
//    text-align: center;
//    background: #5fb878;
//    color: white;
//    height: 100px;
//  }
//
//  .dashboard .layui-side {
//    display: flex;
//    background: #5fb878;
//    align-items: center;
//    justify-content: center;
//    color: white;
//    height: 100%;
//  }
//
//  .layui-body {
//    display: flex;
//    align-items: center;
//    justify-content: center;
//    color: white;
//    height: calc(100vh - 100px);
//  }
//
//  .layui-footer {
//    align-items: center;
//    justify-content: center;
//  }
//}
</style>
