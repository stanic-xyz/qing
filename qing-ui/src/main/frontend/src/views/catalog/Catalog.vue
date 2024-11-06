<style scoped lang="scss">
@import url("@/style/catalog.scss");
</style>

<template>
  <div class="x-container content-wrapper">
    <div class="anime-info-item" v-for="(anime, index) in animeInfoList" :key="index">
      <div class="anime-content-cover">
        <router-link :to="`/anime/${anime.id}`">
          <img :alt="anime.name" :src="anime.cover" :title="anime.name" class="video_thumbs" referrerpolicy="no-referrer"/>
        </router-link>
      </div>
      <div class="anime-content-detail">
        <h5 class="anime-content-detail-title">
          <a :href="`/anime/${anime.id}`">{{ anime.name }}</a>
        </h5>
        <div class="anime-content-detail-info">
          <span>其他名称：</span>
          {{ anime.name }}
        </div>
        <div class="anime-content-detail-info">
          <span>其他名称：</span>
          {{ anime.name }}
        </div>
        <div class="anime-content-detail-info">
          <span>其他名称：</span>
          {{ anime.name }}
        </div>
        <div class="anime-content-detail-info">
          <span>首播时间：</span>
          {{ anime.premiereDate }}
        </div>
        <div class="anime-content-detail-info">
          <span>播放状态：</span>
          {{ anime.playStatus }}
        </div>
        <div class="anime-content-detail-info">
          <span>原作：</span>
          {{ anime.author }}
        </div>
        <div class="anime-content-detail-info">
          <span>剧情类型：</span>
          {{ anime.plotType }}
        </div>
        <div class="anime-content-detail-info">
          <span>制作公司：</span>
          {{ anime.companyName }}
        </div>
        <div class="anime-content-detail-info anime-content-detail-info-description">
          <span>简介：</span>
          {{ anime.instruction }}
        </div>

        <div class="anime-content-detail-buttongroup">
          <button class="anime-content-detail-button">测试内容</button>
          <button class="anime-content-detail-button play">在线播放</button>
        </div>
      </div>
    </div>
    <lay-page v-model="currentPage" :limit="currentLimit" :limits="limits" :total="total" @change="change" theme="black"></lay-page>
  </div>
</template>

<script lang="ts" setup>
import {onMounted, ref} from "vue";
import type {Anime} from "@/apis/anime/types";
import {page} from "@/apis/anime";

const currentLimit = ref(10);
const total = ref(500);
const currentPage = ref(1);
const limits = ref([10, 20, 30, 40, 50]);
const animeInfoList = ref<Anime[]>([]);

function reload() {
  page({
    pageSize: currentLimit.value,
    page: currentPage.value - 1 || 0,
  })
          .then(function (response) {
            console.log("获取到动漫信息内容", response.result);
            animeInfoList.value = response.result.content || [];
            total.value = response.result.totalElements || 0;
          })
          .catch(function (error) {
            console.log("异常了", error);
          });
}

onMounted(() => {
  reload();
});

const change = ({current, limit}) => {
  console.info("current:" + current + " limit:" + limit);
  currentPage.value = current;
  currentLimit.value = limit;
  reload();
};
</script>
