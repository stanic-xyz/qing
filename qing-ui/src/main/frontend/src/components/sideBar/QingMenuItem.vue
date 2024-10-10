<script lang="ts" setup>
import type {MenuItem} from "@/apis/menu/types";
import type {PropType} from "vue";

const {menuItem} = defineProps({
  menuItem: {
    type: Object as PropType<MenuItem>,
    required: true,
  },
});
</script>

<template>
  <template v-if="menuItem.children && menuItem.children.length && menuItem.children.length > 0">
    <lay-sub-menu v-if="menuItem.children && menuItem.children.length > 0" :id="menuItem.id" :title="menuItem.menuName">
      <template #title>
        <lay-icon type="layui-icon-home"></lay-icon>
        {{ menuItem.menuName }}
      </template>
      <template v-if="menuItem.children.length > 0">
        <qing-menu-item v-for="child in menuItem.children" :key="child.id" :menu-item="child"></qing-menu-item>
      </template>
    </lay-sub-menu>
  </template>
  <template v-else>
    <lay-menu-item :id="menuItem.id" :title="menuItem.menuName">
      <router-link :to="menuItem.path">
        <lay-icon type="layui-icon-home"></lay-icon>
        {{ menuItem.menuName }}
      </router-link>
    </lay-menu-item>
  </template>
</template>

<style scoped></style>
