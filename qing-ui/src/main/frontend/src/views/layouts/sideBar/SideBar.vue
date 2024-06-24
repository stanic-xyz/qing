<template>
  <lay-menu :selected-key="selectedKey" @change-selected-Key="changeSelectedKey" @change-open-keys="changeOpenKeys" v-model:openKeys="openKeys2" :tree="true">
    <lay-menu-item id="1">
      <router-link :to="`/dashboard`"></router-link>
    </lay-menu-item>
    <lay-menu-item id="2">首页</lay-menu-item>
    <lay-menu-item id="3">首页</lay-menu-item>
    <lay-sub-menu id="7">
      <template #title> 首页</template>
      <lay-menu-item id="8">首页</lay-menu-item>
      <lay-menu-item id="9">首页</lay-menu-item>
      <lay-sub-menu title="菜单" id="10">
        <template #title> 首页</template>
        <lay-menu-item id="11">首页信息</lay-menu-item>
        <lay-menu-item id="12">首页</lay-menu-item>
        <lay-sub-menu title="菜单" id="13">
          <template #title> 首页</template>
          <lay-menu-item id="14">首页</lay-menu-item>
          <lay-menu-item id="15">首页</lay-menu-item>
          <lay-sub-menu title="菜单" id="16">
            <template #title> 首页</template>
            <lay-menu-item id="17">首页</lay-menu-item>
            <lay-menu-item id="18">首页</lay-menu-item>
            <lay-menu-item id="19">首页</lay-menu-item>
          </lay-sub-menu>
        </lay-sub-menu>
      </lay-sub-menu>
    </lay-sub-menu>
  </lay-menu>
</template>

<script lang="ts" setup>
import { onMounted, ref } from "vue";

const openKeys2 = ref(["7"]);
const selectedKey = ref("5");
const changeSelectedKey = (val: any) => {
  selectedKey.value = val;
  console.log(selectedKey.value);
};

const changeOpenKeys = (val: any) => {
  openKeys2.value = val;
  console.log(openKeys2.value);
};

onMounted(() => {
  console.log("Header加载完毕！");
});
</script>

<style></style>
