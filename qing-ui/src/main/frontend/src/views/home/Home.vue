<script lang="ts" setup>
import { onMounted, ref } from "vue";
import { getAnimeList } from "@/api/anime";
import AnimeInfo from "@/views/anime/AnimeInfo.vue";
import { LayEmpty } from "@layui/layui-vue";
import BlockTitle from "@/views/common/BlockTitle.vue";
import type { Anime } from "@/api/anime/types";

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
  console.log("获取动漫卡片数据");
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

  //   请求 https://www.codegeex.cn/ 网站，获取返回响应状态、响应头、响应内容，并输出
}
</script>

<template>
  <!--  生成一个容器-->
  <lay-container id="container" fluid>
    <lay-layout>
      <lay-body>
        <div>
          <div class="blocktitle">
            <a href="recommend">每日推荐</a>
          </div>
          <div>
            <ul>
              <li v-for="(anime, index) in animeInfoList" :key="index">
                <AnimeInfo :anime="anime"></AnimeInfo>
              </li>
            </ul>
          </div>
          <lay-empty
            v-if="animeInfoList.length == 0"
            description="今日暂无推荐信息"
          ></lay-empty>
          <BlockTitle name="最近播放">最近播放</BlockTitle>
          <div>
            <ul>
              <li
                v-for="(anime, index) in animeInfoList"
                :key="index"
                class="anime_icon1"
              >
                <AnimeInfo :anime="anime"></AnimeInfo>
              </li>
            </ul>
          </div>
        </div>
      </lay-body>
      <lay-side>
        <div>
          <div class="blocktitle">周播列表</div>
          <div>
            <ul id="new_anime_btns">
              <li
                v-for="(week, index) in weekList"
                id="new_anime_page_btn_1"
                :key="index"
                :class="{
                  new_anime_btn_current: activeWeekIndex === index,
                }"
                class="new_anime_btn highlighttag"
                @click="changeWeek(index)"
              >
                {{ week.name }}
              </li>
            </ul>

            <ul id="new_anime_page">
              <li
                v-for="(anime, index) in animeInfoList"
                :key="index"
                class="one_new_anime"
              >
                <a
                  :href="`/anime/${anime.animeId}/index.html'`"
                  class="one_new_anime_name"
                  >{{ anime.name }}</a
                >
                <a
                  :href="`/anime/${anime.id}/index.html'}`"
                  class="one_new_anime_ji"
                  >{{ anime.name }}</a
                >
                <div v-show="true" class="one_anime_new">new!</div>
              </li>
            </ul>
          </div>
          <hr class="hrspace clear" style="width: 95%" />
          <div class="blocktitle">最近更新</div>
          <div class="blockcontent">
            <ul id="anime_update">
              <li
                v-for="(anime, index) in animeInfoList"
                :key="index"
                class="one_new_anime"
              >
                <a class="one_new_anime_name" style="width: 220px">
                  {{ anime.name }}
                </a>
                <span
                  class="anime_update_date asciifont"
                  style="margin-left: 12px"
                  >{{ anime.premiereDate }}</span
                >
              </li>
            </ul>
          </div>
        </div>
      </lay-side>
    </lay-layout>
  </lay-container>
  <!-- 底部 -->
</template>

<style scoped src="../../assets/css/index.css"></style>
