<style lang="scss" scoped>
@import url("@/style/detail.scss");
</style>

<template>
  <div class="x-container content-wrapper">
    <div class="div-left">
      <div class="cover">
        <img :alt="data.anime.name" class="poster" height="356px" referrerpolicy="no-referrer" :src="data.anime.cover" width="256px"/>
      </div>
      <div class="summary">
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

      <div class="detail-info">
        <div class="detail-info-title">基本信息</div>
        <ul class="detail-info-content">
          <li class="detail-info-content-item">
            <span class="detail-info-content-item-tag">地区：</span>
            <span class="detail-info-content-item-value">{{ data.anime.name }}</span>
          </li>
          <li class="detail-info-content-item">
            <span class="detail-info-content-item-tag">动画种类：</span>
            <span class="detail-info-content-item-value">{{ data.anime.typeName }}</span>
          </li>
          <li class="detail-info-content-item">
            <span class="detail-info-content-item-tag">动画名称：</span>
            <span class="detail-info-content-item-value">{{ data.anime.name }}</span>
          </li>
          <li class="detail-info-content-item">
            <span class="detail-info-content-item-tag">原版名称：</span>
            <span class="detail-info-content-item-value">{{ data.anime.originalName }}</span>
          </li>
          <li class="detail-info-content-item">
            <span class="detail-info-content-item-tag">作者：</span>
            <span class="detail-info-content-item-value">{{ data.anime.author }}</span>
          </li>
          <li class="detail-info-content-item">
            <span class="detail-info-content-item-tag">制作公司：</span>
            <span class="detail-info-content-item-value">{{ data.anime.companyName }}</span>
          </li>
          <li class="detail-info-content-item">
            <span class="detail-info-content-item-tag">首播时间：</span>
            <span class="detail-info-content-item-value">{{ data.anime.premiereDate }}</span>
          </li>
          <li class="detail-info-content-item">
            <span class="detail-info-content-item-tag">播放状态：</span>
            <span class="detail-info-content-item-value">{{ data.anime.playStatus }}</span>
          </li>
          <li class="detail-info-content-item">
            <span class="detail-info-content-item-tag">剧情类型：</span>
            <span class="detail-info-content-item-value">{{ data.anime.plotType }}</span>
          </li>
          <li class="detail-info-content-item">
            <span class="detail-info-content-item-tag">标签：</span>
            <span class="detail-info-content-item-value">{{ data.anime.tagIds }}</span>
          </li>
        </ul>
      </div>
    </div>
    <div class="div-right">
      <div class="description">
        <div class="detail-info-name">
          <h2>
            {{ data.anime.name }}
          </h2>
        </div>
        <hr class="detail-info-divider" />
        <div class="baseblock">
          <div class="blockcontent">
            <div class="detail_imform_desc_pre">
              <p>{{ data.anime.instruction }}</p>
            </div>
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
        <hr />
        <div class="playlist-content">
          <div class="playlist-content-tab">
            <div class="playlist-content-tab-item" :class="{ selected: selectTab == list.id }" :title="list.name" id="1" v-for="(list, index) of data.anime.playLists" :key="index" @click="choseTab(list.id)">
              <lay-icon type="layui-icon-gitee" class="playlist-title-icon"></lay-icon>
              {{ list.name }}
            </div>
          </div>

          <div class="playlist-content-content" v-for="(list, index) in data.anime.playLists" :key="index" v-show="selectTab === list.id">
            <ul>
              <li class="playlist-content-playlist-episode-item" v-for="(episode, episodeIndex) in list.episodeList" :key="episodeIndex">
                <router-link :to="`/play/${data.anime.id}/${list.id}/${episode.id}`">
                  <span>{{ episode.name }}</span>
                </router-link>
                <div class="playing">播放中</div>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import {onMounted, reactive, ref} from "vue";
import {findDetailById} from "@/apis/anime";
import {useRoute, useRouter} from "vue-router";
import type {AnimeDetail} from "@/apis/anime/types";

const selectTab = ref(1);

const data = reactive({
  activeIndex: "1",
  activeIndex2: "1",
  currentDate: new Date(),
  menuIndex: 0,
  time: "2020年01月15日 21时50分19秒",
  loading: true,
  anime: {} as AnimeDetail,
  reportTypes: [
    {
      id: "1",
      name: "反馈类型1",
    },
  ],
});

onMounted(() => {
  const id = useRoute().params.animeId;
  console.log("加载了id", id);
  if (typeof id === "string") {
    findDetailById(Number.parseInt(id))
      .then((response) => {
        console.log("获取到了结果了", response.result);
        data.anime = response.result;
        if (response.result.playLists.length > 0) {
          selectTab.value = response.result.playLists.at(0).id;
        }
      })
      .catch((error) => {
        console.log("为查询到动漫信息", error.code, error.response.data.message);
      });
  } else {
    // 参数错误
    useRouter().push("/error/paramError");
  }
});

function choseTab(playListId: number) {
  selectTab.value = playListId;
}
</script>
