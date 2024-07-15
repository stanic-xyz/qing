<template>
  <lay-menu v-model:openKeys="openKeys2" :collapse-transition="true" :indent="true" :level="true" :selected-key="selectedKey" :tree="true" @change-selected-Key="changeSelectedKey" @change-open-keys="changeOpenKeys">
    <template v-for="{ menuId, menuName, children } of menuItems">
      <lay-sub-menu :id="menuId">
        <template #title>
          <lay-icon type="layui-icon-home"/>
          {{ menuName }}
        </template>
        <lay-menu-item v-for="{ menuId, menuName } of children" :id="menuId">{{ menuName }}</lay-menu-item>
      </lay-sub-menu>
    </template>
  </lay-menu>
</template>

<script lang="ts" setup>
import {onMounted, ref} from "vue";
import {menuTree} from "@/apis/menu";
import type {MenuItem} from "@/apis/menu/types";

const openKeys2 = ref(["7"]);
const selectedKey = ref("5");
const menuItems = ref([{} as MenuItem]);

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
  menuTree().then((response) => {
    console.debug("读取菜单配置", response);
    menuItems.value = response.result;
    console.debug("菜单配置成功", menuItems.value);
    openKeys2.value = menuItems.value.map((menu) => menu.menuId);
    selectedKey.value = menuItems.value[0].menuId;
  });
});
</script>

<style></style>
