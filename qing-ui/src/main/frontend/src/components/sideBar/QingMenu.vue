<script lang="ts" setup>
import type {MenuItem} from "@/apis/menu/types";
import QingMenuItem from "@/components/sideBar/QingMenuItem.vue";
import {type PropType, ref} from "vue";

const openKeys2 = ref(["7"]);
const selectedKey = ref("5");

const {menuItems} = defineProps({
  menuItems: {
    type: Array as PropType<Array<MenuItem>>,
    required: true,
  },
});

console.log("获取到的参数列表", menuItems.at(0)?.children);
</script>

<template>
  <lay-menu v-model:openKeys="openKeys2" :collapse-transition="true" :level="true" :selected-key="selectedKey" :tree="true">
    <template v-for="menuItem of menuItems" :key="menuItem.id">
      <qing-menu-item :menu-item="menuItem"></qing-menu-item>
    </template>
  </lay-menu>
</template>

<style scoped></style>
