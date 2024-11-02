<style scoped lang="scss">
@import url("@/assets/css/common/base-css.css");

.content-wrapper {
  width: 100%;
  display: flex;
  flex-direction: column;

  .layui-page {
    color: white;

    .layui-page-limits {
      color: white;
    }
  }

  .anime-info-item {
    width: 100%;
    border-radius: 10px;
    padding: 10px 10px;
    margin-bottom: 10px;
    display: flex;
    flex-direction: row;
    border: 1px solid rgb(1, 1, 33);

    .anime-content-cover {
      width: 172px;

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

        span {
          color: #808081;
        }
      }

      .anime-content-detail-info-description {
        margin-bottom: 0.5rem;
        overflow: hidden;
        text-overflow: ellipsis;
        display: -webkit-box;
        line-clamp: 2;
        -webkit-line-clamp: 2;
        -webkit-box-orient: vertical;
      }

      .anime-content-detail-buttongroup {
        width: 100%;
        text-align: left;

        .anime-content-detail-button {
          padding: 5px;
          margin-right: 10px;
          border-radius: 4px;
          cursor: pointer;
          background-color: #292121;
          border: 1px solid black;
          color: white;

          &:hover {
            background-color: red;
            border: 1px solid red;
          }
        }

        .play {
          background-color: red;
          color: white;
          border: 1px solid red;

          &:hover {
            background-color: darkred;
            border: 1px solid darkred;
          }
        }
      }
    }
  }
}
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
  console.log("加载页面完成，请求数据");
  reload();
});

const change = ({current, limit}) => {
  console.info("current:" + current + " limit:" + limit);
  currentPage.value = current;
  currentLimit.value = limit;
  reload();
};
</script>
