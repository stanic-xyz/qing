<template>
  <div id="container">
    <div class="baseblock" style="margin: 0 10px">
      <div v-for="(anime, index) in animeInfoList" :key="index">
        <div class="card" style="display: flex; margin: 1rem 0">
          <router-link :to="`/anime/${anime?.id}`" class="cell_poster">
            <img alt="发布信息" class="anime_icon1_img" height="auto" referrerpolicy="no-referrer" src="https://cdn.aqdstatic.com:966/age/20180073_small.jpg" style="width: auto; height: auto" width="auto"/>
            />
          </router-link>
          <div class="card-content" style="flex: 1; padding-left: 4px">
            <div>
              <router-link :to="`/anime/${anime.id}`" class="cell_imform_name">
                <h2>
                  {{ anime.name }}
                </h2></router-link
              >
            </div>
            <div class="vedio_detail_infos">
              <div class="vedio_detail_info">
                <span>动画种类：</span>
                {{ anime.typeName }}
              </div>
              <div class="vedio_detail_info">
                <span>原版名称：</span>
                {{ anime.originalName }}
              </div>
              <div class="vedio_detail_info">
                <span>其他名称：</span>
                {{ anime.otherName }}
              </div>
              <div class="vedio_detail_info">
                <span>首播时间：</span>
                {{ anime.premiereDate }}
              </div>
              <div class="vedio_detail_info">
                <span>播放状态：</span>
                {{ anime.playStatus }}
              </div>
              <div class="vedio_detail_info">
                <span>原作：</span>
                {{ anime.author }}
              </div>
              <div class="vedio_detail_info">
                <span>剧情类型：</span>
                {{ anime.plotType }}
              </div>
              <div class="vedio_detail_info">
                <span>制作公司：</span>
                {{ anime.companyName }}
              </div>
              <div class="vedio_detail_info vedio_detail_info_desc">
                <span>简介</span>
                <div class="cell_imform_desc">
                  {{ anime.instruction }}
                </div>
              </div>
            </div>
            <div class="cell_imform_btns">
              <router-link :to="`/play/${anime.id}`" class="nbutton2 cell_res_button">资源详情</router-link>
              <router-link :to="`play/${anime.id}`" class="nbutton2 cell_res_button">在线播放</router-link>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="baseblock">
      <div class="blockcontent">
        <span class="asciifont result_count">{{ 1 }}记录</span>
      </div>
    </div>
    <LayPage v-model="currentPage" :limit="limit" :show-page="true" :total="total"></LayPage>
  </div>
</template>

<style lang="less" scoped src="../../assets/css/catalog.css">
#container {
  margin: 0 auto;
  padding: 0 0;
}

.vedio_detail_infos {
  .vedio_detail_info {
    min-width: 50%;
    display: inline-block;

    span {
      color: lightgray;
    }
  }
}
</style>

<script lang="ts" setup>
import {onMounted, ref} from "vue";
import type {Anime} from "@/apis/anime/types";
import {page} from "@/apis/anime";

const animeInfoList = ref<Anime[]>([]);
const limit = ref(20);
const total = ref(100);
const currentPage = ref(2);

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
            console.log(error);
    });
});
</script>
