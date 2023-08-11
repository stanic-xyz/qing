<script lang="ts" setup>
import { onMounted, ref } from "vue";
import type { Anime } from "@/api/anime";
import { getAnimeList } from "@/api/anime";
import AnimeInfo from "@/views/anime/AnimeInfo.vue";
import { LayEmpty } from "@layui/layui-vue";
import BlockTitle from "@/views/common/BlockTitle.vue";

const pagination = ref({ current: 1, pageSize: 12, total: 0 });

const animeInfoList = ref<Anime[]>([]);
console.log("加载数据", "onMounted");
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

const page = ref({ total: 100, limit: 10, current: 2 });

const dataSource = [
  {
    id: "1",
    username: "shana",
    password: "夏娜",
    remark: "花开堪折直须折,莫待无花空折枝",
    age: "22",
  },
  {
    id: "2",
    username: "shana",
    password: "夏娜",
    remark: "花开堪折直须折,莫待无花空折枝",
    age: "22",
  },
  {
    id: "3",
    username: "shana",
    password: "夏娜",
    remark: "花开堪折直须折,莫待无花空折枝",
    age: "22",
  },
  {
    id: "4",
    username: "shana",
    password: "夏娜",
    remark: "花开堪折直须折,莫待无花空折枝",
    age: "22",
  },
  {
    id: "5",
    username: "shana",
    password: "夏娜",
    remark: "花开堪折直须折,莫待无花空折枝",
    age: "22",
  },
  {
    id: "6",
    username: "shana",
    password: "夏娜",
    remark: "花开堪折直须折,莫待无花空折枝",
    age: "22",
  },
  {
    id: "7",
    username: "shana",
    password: "夏娜",
    remark: "花开堪折直须折,莫待无花空折枝",
    age: "22",
  },
  {
    id: "8",
    username: "shana",
    password: "夏娜",
    remark: "花开堪折直须折,莫待无花空折枝",
    age: "22",
  },
];

interface AnimeDetailInfo {
  animeName?: string;
}

const getCardListData = async () => {
  console.log("获取动漫卡片数据");
  var newVar = await getAnimeList();
  console.log("取得值了", newVar.data.content);
  pagination.value = {
    ...pagination.value,
    total: 2,
  };
  animeInfoList.value = newVar.data.content;
};

onMounted(() => {
  console.debug("onMounted，", "加载动漫信息");
  getCardListData();
});

function changeWeek(id: Number) {
  activeWeekIndex.value = id;
}
</script>

<template>
  <lay-container fluid id="container">
    <lay-layout>
      <lay-body>
        <div class="div_left baseblock">
          <div class="blocktitle">
            <a href="recommend">每日推荐</a>
          </div>
          <div class="blockcontent">
            <ul class="ul_li_a5">
              <li
                v-for="(anime, index) in animeInfoList"
                :key="index"
                class="anime_icon1"
              >
                <AnimeInfo :anime="anime"></AnimeInfo>
              </li>
            </ul>
          </div>
          <lay-empty
            description="今日暂无推荐信息"
            v-if="animeInfoList.length == 0"
          ></lay-empty>
          <BlockTitle name="最近播放">最近播放</BlockTitle>
          <div class="blockcontent">
            <ul class="ul_li_a5">
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
        <div class="div_right baseblock">
          <div class="blocktitle">周播列表</div>
          <div class="blockcontent">
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
</template>

<style scoped src="../../assets/css/index.css"></style>
