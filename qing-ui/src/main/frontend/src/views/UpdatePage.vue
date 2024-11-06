<style scoped lang="scss">
@import url("@/style/update.scss");
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
          <router-link :to="`/anime/${anime.id}`">
            <button class="anime-content-detail-button">测试内容</button>
          </router-link>
          <router-link :to="`/play/${anime.id}`">
            <button class="anime-content-detail-button play">在线播放</button>
          </router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import {onMounted, ref} from "vue";
import type {Anime} from "@/apis/anime/types";
import {page} from "@/apis/anime";
import {useRouter} from "vue-router";

const animeInfoList = ref<Anime[]>([]);

const router = useRouter();

onMounted(() => {
  page({
    pageSize: 10,
    page: 0,
  })
          .then(function (response) {
            console.log("获取到动漫信息内容", response.result.content || []);
            animeInfoList.value = response.result.content || [];
          })
          .catch(function (error) {
            console.log("异常了", error);
          });
});

function play(id: number) {
  router.push({
    name: "play",
    params: {
      animeId: id,
    },
  });
}
</script>
