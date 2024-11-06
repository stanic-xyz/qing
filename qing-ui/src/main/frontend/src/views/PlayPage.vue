<style scoped lang="scss">
@import url("@/style/play.scss");
</style>

<template>
  <div class="x-container container">
    <div class="age-frame-div">
      <my-player :likes="1" :url="data.videoUrl"></my-player>
    </div>
    <div class="operation">
      <div class="operation-content">
        <div class="summary-item">
          <lay-icon type="layui-icon-fire" class="fire"></lay-icon>
          20W
        </div>
        <div class="summary-item">
          <lay-icon type="layui-icon-dialogue" class="fire"></lay-icon>
          494
        </div>
        <div class="summary-item">
          <lay-icon type="layui-icon-heart" class="fire"></lay-icon>
          2,363
        </div>
      </div>
      <div class="mobile-player">手机播放</div>
    </div>
    <div class="anime-info-item">
      <div class="anime-content-cover">
        <img width="172px" :alt="anime.name" :src="anime.cover" :title="anime.name" class="video_thumbs" referrerpolicy="no-referrer"/>
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
      </div>
    </div>

    <div class="playlist-wrapper">
      <div class="playlist-header">
        <div>
          <span>
            <lay-icon type="layui-icon-play" class="playlist-title-icon"></lay-icon>
            <span class="playlist-title">在线播放</span>
          </span>
        </div>
        <small class="playlist-notice">视频如果未正常播放或者卡顿，请切换播放源，优先选择 VIP 播放源!</small>
      </div>
      <hr/>
      <div class="playlist-content">
        <div class="playlist-content-tab">
          <div class="playlist-content-tab-item" :class="{ selected: currentListId == list.id }" :title="list.name" id="1" v-for="(list, index) of anime.playLists" :key="index" @click="choseTab(list.id)">
            <lay-icon type="layui-icon-gitee" class="playlist-title-icon"></lay-icon>
            {{ list.name }}
          </div>
        </div>

        <div class="playlist-content-content" v-for="(list, index) in anime.playLists" :key="index" v-show="currentListId === list.id">
          <ul>
            <li class="playlist-content-playlist-episode-item" v-for="(episode, episodeIndex) in list.episodeList" :key="episodeIndex">
              <router-link :to="`/play/${anime.id}/${list.id}/${episode.id}`">
                <span :class="{ selected: currentListId === list.id && currentEpisodeId === episode.id }">{{ episode.name }}</span>
              </router-link>
              <div class="playing">播放中</div>
            </li>
          </ul>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import {onMounted, reactive, ref, watch} from "vue";
import MyPlayer from "@/components/MyPlayer.vue";
import type {AnimeDetail} from "@/apis/anime/types";
import {findDetailById} from "@/apis/anime";
import {useRoute, useRouter} from "vue-router";

const anime = ref<AnimeDetail>({} as AnimeDetail);

const route = useRoute();
const router = useRouter();

const data = reactive({
  activeIndex: "1",
  activeIndex2: "1",
  currentDate: new Date(),
  time: "2020年01月15日 21时50分19秒",
  loading: true,
  videoUrl: "http://192.168.3.52:9000/qing/sunset.mp4",
});

const currentAnimeId = ref(0);
const currentListId = ref(0);
const currentEpisodeId = ref(0);

onMounted(() => {
  console.log("首次加载路由", route.fullPath);
  const animeId = Number(route.params.animeId);
  const listId = Number(route.params.listId);
  const episodeId = Number(route.params.episodeId);

  currentAnimeId.value = animeId;
  currentListId.value = listId;
  currentEpisodeId.value = episodeId;

  findDetailById(animeId).then((response) => {
    anime.value = response.result;
    if (!currentListId.value) {
      console.log("跳转到新的路由", animeId);
      currentListId.value = anime.value.playLists[0].id || null;
      currentEpisodeId.value = anime.value.playLists[0].episodeList[0].id || null;
      router.push(`/play/${animeId}/${currentListId.value}/${currentEpisodeId.value}`);
    }
  });
});

watch(
        () => route.fullPath,
        (prev, current) => {
          console.log("路由发生了变化", prev, current);
          const listId = Number(route.params.listId);
          const episodeId = Number(route.params.episodeId);

          console.log("参数信息", currentAnimeId.value, listId, episodeId);
          currentListId.value = listId;
          currentEpisodeId.value = episodeId;
        },
);

function choseTab(playListId: number) {
  currentListId.value = playListId;
}
</script>
