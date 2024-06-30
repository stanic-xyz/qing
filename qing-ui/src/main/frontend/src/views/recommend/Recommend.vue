<style scoped src="../../assets/css/play.css"></style>

<template>
  <div>
    <div class="spaceBlock"></div>
    <div class="baseBlock">
      <div class="blockContent">
        <ul class="ul_li_a6">
          <li v-if="animeInfoList.length !== 0" v-for="(anime, index) in animeInfoList" :key="index" class="anime_icon2">
            <router-link :to="`/anime/${anime.id}`">
              <img :alt="anime.name" :src="anime.coverUrl" :title="anime.name" class="anime_icon2_img" height="208px" referrerpolicy="no-referrer" width="150px" />
              <span class="anime_icon1_name1">[TV 01-12]</span>
            </router-link>
            <h4 class="anime_icon2_name">
              <router-link :to="`anime/${anime.id}`">{{ anime.name }}</router-link>
            </h4>
          </li>
          <li v-if="animeInfoList.length === 0" class="anime_icon2">
            <router-link :to="`/anime/123123`">
              <img :alt="`12312`" :src="`12312`" :title="`12312`" class="anime_icon2_img" height="208px" referrerpolicy="no-referrer" width="150px"/>
              <span class="anime_icon1_name1">[TV 01-12]</span>
            </router-link>
            <h4 class="anime_icon2_name">
              <router-link :to="`anime/12312`">{{ "anime.name" }}</router-link>
            </h4>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import {onMounted, ref} from "vue";
import type {Anime} from "@/apis/anime/types";
import {page} from "@/apis/anime";

const animeInfoList = ref<Anime[]>([]);

onMounted(() => {
  page({
    pageSize: 10,
    page: 0,
  })
          .then(function (response) {
            console.log("获取到动漫信息内容", response.result.list || []);
            animeInfoList.value = response.result.list || [];
          })
          .catch(function (error) {
            console.log("异常了", error);
          });
});
</script>
