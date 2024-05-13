<template>
  <!-- 首页-->
  <div style="display: flex; flex-direction: row">
    <div class="h-full">
      <div class="h-full">左边部分</div>
      <div class="h-full">左边部分</div>
    </div>
    <div class="h-full">右边的更新内容去</div>
  </div>
  <!-- 底部 -->
</template>

<script lang="ts" setup>
import {onMounted, ref} from "vue";
import {getAnimeList} from "@/apis/anime";
import type {Anime} from "@/apis/anime/types";

const pagination = ref({ current: 0, pageSize: 12, total: 0 });
const animeInfoList = ref<Anime[]>([]);
const weekList = ref([
  {
    weekId: 1,
    name: "周一",
  },
  {
    weekId: 2,
    name: "周二",
  },
  {
    weekId: 3,
    name: "周三",
  },
  {
    weekId: 4,
    name: "周四",
  },
  {
    weekId: 5,
    name: "周五",
  },
  {
    weekId: 6,
    name: "周六",
  },
  {
    weekId: 7,
    name: "周日",
  },
]);

const activeWeekIndex = ref<Number>(0);

const getUserInfo = async () => {
  console.log("用户当前登录状态", false);
  // const promise = await guard.startWithRedirect();
};

const getCardListData = async () => {
  console.log("获取动漫卡片数据：开始请求");
  const animeListResponse = await getAnimeList({
    page: pagination.value.current,
    pageSize: pagination.value.pageSize,
  });
  const result = animeListResponse.result;
  pagination.value.total = result.total;
  console.log("请求结束后的分页信息,总页数：", pagination.value.total);
  animeInfoList.value = result.list;
};

onMounted(() => {
  getUserInfo();
  console.debug("onMounted");
  getCardListData();
});

function changeWeek(id: Number) {
  activeWeekIndex.value = id;
}
</script>

<style scoped>
@import url("@/assets/css/common/base.css");
</style>
