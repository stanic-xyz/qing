<template>
  <header id="nav">
    <form
      id="top_search_from"
      class="search_form"
      method="get"
      @submit="handleSelect"
    >
      <label for="top_search_input"></label>
      <input
        id="top_search_input"
        autocapitalize="on"
        autocomplete="on"
        class="search_input"
        name="keyword"
        placeholder="输入关键字"
        spellcheck="false"
        value=""
      />
      <input class="search_submit" type="submit" value="" />
      <input name="page" type="hidden" value="1" />
    </form>
  </header>
</template>
<script lang="ts" setup>
import { reactive } from "vue";

defineProps<{
  title?: string;
}>();

function handleSelect(payload: Event) {
  console.log(payload.AT_TARGET);
}

reactive({
  activeIndex: "1",
  navList: [
    { groupName: "首页", route: "/", groupIcon: "", groupActive: true },
    {
      groupName: "目录",
      route: "/catalog",
      groupIcon: "",
      groupActive: false,
    },
    {
      groupName: "每日推荐",
      route: "/recommend",
      groupIcon: "",
      groupActive: false,
    },
    {
      groupName: "最近更新",
      route: "/update",
      groupIcon: "",
      groupActive: false,
    },
    {
      groupName: "排行榜",
      route: "/rank",
      groupIcon: "",
      groupActive: false,
    },
    {
      groupName: "详情",
      route: "/detail",
      groupIcon: "",
      groupActive: false,
    },
    {
      groupName: "搜索",
      route: "/search",
      groupIcon: "",
      groupActive: false,
    },
  ],
});
</script>
<style scoped></style>
