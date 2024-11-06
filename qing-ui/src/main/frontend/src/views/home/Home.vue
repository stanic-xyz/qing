<template>
  <!-- 首页-->
  <div class="x-container content-wrapper notice" style="text-align: center">
    <mark>AGE动漫 备用地址：<a style="color: red">www.stanic.xyz</a> 欢迎大家分享给身边朋友！为确保正常观看，请使用 谷歌浏览器</mark>
  </div>
  <div class="x-container content-wrapper">
    <div class="content-panel">
      <div class="update">
        <div class="update-header">
          <span class="update-header-title">最近更新</span>
          <button class="update-header-more">更多>></button>
        </div>
        <div class="update-body">
          <ul>
            <li v-for="anime of animeInfoList" :key="anime.id">
              <router-link :to="`/anime/${anime.id}`">
                <div class="anime">
                  <img :src="anime.cover" alt="" />
                  <span>{{ anime.name }}</span>
                </div>
              </router-link>
            </li>
          </ul>
        </div>
      </div>
      <div class="update">
        <div class="update-header">
          <span class="update-header-title">本周推荐</span>
          <button class="update-header-more">更多>></button>
        </div>
        <div class="update-body">
          <ul>
            <li v-for="anime of animeInfoList" :key="anime.id">
              <router-link :to="`/anime/${anime.id}`">
                <div class="anime">
                  <img :src="anime.cover" alt="" />
                  <span>{{ anime.name }}</span>
                </div>
              </router-link>
            </li>
          </ul>
        </div>
      </div>
    </div>
    <div class="content-right">
      <div class="content-right-title">本周放送列表</div>
      <ul class="content-right-week">
        <li v-for="week of weekList" :class="{ current: activeWeekIndex === week.weekId }" :key="week.weekId" @click="changeWeek(week.weekId)">{{ week.name }}</li>
      </ul>
      <div>
        <ul>
          <li v-for="anime of animeInfoList" :key="anime.id">
            <div class="list-item">
              <router-link class="list-item-name" :to="`/anime/${anime.id}`">
                <span>{{ anime.name }}</span>
              </router-link>
              <span>00：59 第15集</span>
            </div>
          </li>
        </ul>
      </div>
      <div class="content-right-title">最近更新</div>
      <div>
        <ul>
          <li v-for="anime of animeInfoList" :key="anime.id">
            <div class="list-item">
              <router-link class="list-item-name" :to="`/anime/${anime.id}`">
                <span>{{ anime.name }}</span>
              </router-link>
              <span>00：59 第15集</span>
            </div>
          </li>
        </ul>
      </div>
    </div>
  </div>
  <!-- 底部 -->
</template>

<style scoped lang="scss">
@import url("@/assets/css/common/base-css.css");
@import url("@/style/home.scss");
</style>

<script lang="ts" setup>
import {onMounted, ref} from "vue";
import {getAnimeList} from "@/apis/anime";
import type {Anime} from "@/apis/anime/types";

const pagination = ref({ current: 0, pageSize: 10, total: 0 });
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

const activeWeekIndex = ref<Number>(1);

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
  animeInfoList.value = result.content;
  console.info("读取到了", animeInfoList.value);
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
