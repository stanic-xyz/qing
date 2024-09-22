<template>
  <qing-menu :menu-items="menuItems"></qing-menu>
</template>

<script lang="ts" setup>
import {onMounted, ref} from "vue";
import {menuTree} from "@/apis/menu";
import type {MenuItem} from "@/apis/menu/types";
import QingMenu from "@/components/sideBar/QingMenu.vue";

const openKeys2 = ref(["7"]);
const selectedKey = ref("5");
const menuItems = ref([{} as MenuItem]);

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
