<style scoped lang="scss">
@import url("@/assets/css/common/base-css.css");

.container {
  display: flex;
  flex-direction: column;
  margin: 1rem 0;

  .age-frame-div {
    width: 100%;
    height: auto;
  }

  .operation {
    background-color: rgb(1, 1, 33);
    color: white;
    height: 2rem;
    line-height: 2rem;
    padding: 0 10px;
    display: flex;
    justify-content: space-between;
    margin: 5px 0;
    border-radius: 5px;

    .operation-content {
      display: inline-flex;
      width: 20%;
      justify-content: space-between;
    }
  }

  .anime-info-item {
    width: 100%;
    padding: 10px 10px;
    margin-bottom: 10px;
    display: flex;
    flex-direction: row;
    border-bottom-left-radius: 10px;
    border-bottom-right-radius: 10px;
    border: 1px solid rgb(1, 1, 33);
    background-color: rgb(1, 1, 33);

    .anime-content-cover {
      width: 172px;
      height: auto;
      overflow: hidden;

      a {
        text-decoration: none;
        display: block !important;
        width: 100%;
        overflow: hidden;
        border-radius: 5px;

        img {
          width: 172px;
          display: inline;
          vertical-align: middle;

          &:hover {
            opacity: 0.7;
            transform: scale(1.1);
          }
        }

        .video_thumbs {
          transition: all linear 0.3s;
        }
      }
    }

    .anime-content-detail {
      width: 100%;
      padding: 0 0 0 1rem;

      .anime-content-detail-title {
        a {
          font-size: 18px;
          color: #d0e0f0;
        }

        &:hover {
          text-decoration: underline;
        }
      }

      .anime-content-detail-info {
        min-width: 50%;
        display: inline-block;
        line-height: 2rem;
        color: #d0e0f0;

        span {
          color: #808081;
        }
      }

      .anime-content-detail-info-description {
        margin-bottom: 0.5rem;
        overflow: hidden;
        text-overflow: ellipsis;
        display: -webkit-box;
      }
    }
  }

  .playlist-wrapper {
    width: 100%;
    color: white;

    .playlist-header {
      width: 100%;
      padding-top: 1rem;
      display: flex;
      flex-direction: row;
      justify-content: space-between;
      align-items: center;
      align-content: center;
      font-size: 1rem;
      color: white;

      .playlist-title-icon {
        color: #dc3545;
      }

      .playlist-title {
        margin-left: 0.5rem;
        color: #dc3545;
      }

      .playlist-notice {
        color: darkgray;
      }
    }
  }

  .playlist-content {
    width: 100%;
    color: white;

    .playlist-content-tab {
      width: 100%;
      display: flex;

      .playlist-content-tab-item {
        background-color: rgb(33, 37, 41);
        line-height: 1rem !important;
        text-align: center;
        padding: 0.5rem;
        color: rgba(255, 255, 255, 0.55);
        border-radius: 10px 10px 0 0;
        width: 100px;
        cursor: pointer;

        &:hover {
          color: white;
        }

        .playlist-title-icon {
          color: darkred;
        }
      }

      .selected {
        background-color: red;
        color: white;
      }
    }

    .playlist-content-content {
      width: 100%;
      min-height: 240px;
      max-height: 500px;
      background-color: rgb(55, 55, 56);
      padding: 1rem;

      .playlist-content-playlist-episode-item {
        display: inline-block;
        border: 1px solid gray;
        border-radius: 6px;
        height: 40px;
        width: 11.111%;
        line-height: 40px;
        text-align: center;
        overflow: hidden;
        cursor: pointer;
        margin-bottom: 2px;
        padding: 0 2px;

        &:hover {
          background-color: #dc3545;
        }

        .selected {
          background-color: #dc3545;
        }

        span {
          display: inline-block;
          width: 100%;
          color: white;
        }
      }
    }
  }
}
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
import {useRoute} from "vue-router";

const anime = ref<AnimeDetail>({} as AnimeDetail);

const router = useRoute();

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
  console.log("首次加载路由", router.fullPath);
  const animeId = Number(router.params.animeId);
  const listId = Number(router.params.listId);
  const episodeId = Number(router.params.episodeId);

  currentAnimeId.value = animeId;
  currentListId.value = listId;
  currentEpisodeId.value = episodeId;

  findDetailById(animeId).then((res) => {
    anime.value = res.result;
  });
});

watch(
        () => router.fullPath,
        (prev, current) => {
          console.log("路由发生了变化", prev, current);
          const listId = Number(router.params.listId);
          const episodeId = Number(router.params.episodeId);

          console.log("参数信息", currentAnimeId.value, listId, episodeId);
          currentListId.value = listId;
          currentEpisodeId.value = episodeId;
        },
);

function choseTab(playListId: number) {
  currentListId.value = playListId;
}
</script>
