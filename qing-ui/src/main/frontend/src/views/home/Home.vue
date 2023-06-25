<script lang="ts" setup>
import { onMounted, ref } from "vue";
import type { Anime } from "@/api/anime";
import { getAnimeList } from "@/api/anime";
import AnimeInfo from "@/views/anime/AnimeInfo.vue";
import { LayEmpty } from "@layui/layui-vue";
import MyHeader from "@/views/common/MyHeader.vue";
import MyFooter from "@/views/common/MyFooter.vue";

const pagination = ref({ current: 1, pageSize: 12, total: 0 });

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
const productList = ref<Anime[]>([]);
const dataLoading = ref<boolean>(true);
const activeWeekIndex = ref<Number>(0);

interface AnimeDetailInfo {
  animeName?: string;
}

const getCardListData = async () => {
  await getAnimeList();

  pagination.value = {
    ...pagination.value,
    total: 2,
  };
};

onMounted(() => {
  getCardListData();
  for (let i = 0; i < 14; i++) {
    animeInfoList.value.push({
      id: 3,
      name: "金好理五候",
      instruction: "ex est fugiat",
      districtId: 90,
      districtName: "持增该山克布",
      coverUrl:
        "http://localhost:8080/img/anime/%E4%BC%A4%E7%89%A9%E8%AF%AD.jpg",
      typeId: 29,
      typeName: "间大决据手阶",
      originalName: "劳特五感长",
      otherName: "联统系在置克",
      author: "consectetur veniam adipisicing Lorem",
      company: "magna ad sint id consequat",
      playStatus: "FINISHED",
      plotType: "pariatur sint sit",
      tags: ["pariatur exercitation irure consequat ad"],
      officialWebsite: "labore consectetur dolore ullamco culpa",
      playHeat: "sit",
    });
  }
});

function changeWeek(id: Number) {
  activeWeekIndex.value = id;
}
</script>

<template>
  <lay-layout class="root">
    <lay-header style="height: auto">
      <MyHeader />
    </lay-header>
    <lay-body>
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
    </lay-body>
    <lay-footer>
      <MyFooter />
    </lay-footer>
  </lay-layout>
</template>

<style scoped src="../../assets/css/index.css"></style>
