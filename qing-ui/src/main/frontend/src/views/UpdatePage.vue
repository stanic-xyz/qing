<template>
  <div id="container">
    <div class="spaceBlock"></div>
    <div class="baseBlock">
      <div class="blockContent">
        <div class="spaceBlock"></div>
        <ul class="ul_li_a6">
          <li
            v-for="(anime, index) in animeList"
            :key="index"
            class="anime_icon2"
          >
            <router-link :to="`/detail/${anime.id}`">
              <img
                :alt="anime.name"
                :src="anime.coverUrl"
                :title="anime.name"
                class="anime_icon2_img"
                height="208px"
                referrerpolicy="no-referrer"
                width="150px"
              />
              <span class="anime_icon1_name1">02:00 第10话</span>
            </router-link>
            <h4 class="anime_icon2_name">
              <router-link :to="`/detail/${anime.id}`">
                {{ anime.name }}
              </router-link>
            </h4>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { onMounted, ref } from "vue";
import type { Anime } from "@/api/anime";

const animeList = ref<Anime[]>([]);

const data = ref({
  activeIndex: "1",
  activeIndex2: "1",
  currentDate: new Date(),
  time: "2020年01月15日 21时50分19秒",
  loading: true,
});

onMounted(() => {
  animeList.value.push({
    id: 20000001,
    name: "海贼王",
    instruction:
      "电视动画《航海王》改编自尾田荣一郎创作的同名长篇少年漫画，动画由东映动画制作。 海贼王哥尔·D·罗杰在临死前曾留下了关于其毕生的财富“One Piece”的消息，由此引得群雄并起，众海盗们为了这笔传说中的巨额财富展开争夺，各种势力、政权不断交替，整个世界进入了动荡混乱的“大海贼时代”。 生长在东海某小村庄的路飞受到海贼香克斯的精神指引，决定成为一名出色的海盗。为了达成这个目标，并找到万众瞩目的One Piece，路飞踏上艰苦的旅程。一路上他遇到了无数磨难，也结识了索隆、娜美、山治、乌索普、罗宾等一众性格各异的好友。他们携手一同展开充满传奇色彩的大冒险。",
    districtId: 1,
    districtName: "日本",
    coverUrl: "http://localhost:8080/img/anime/%E4%BC%A4%E7%89%A9%E8%AF%AD.jpg",
    typeId: 1,
    typeName: "TV",
    originalName: "ONE PIECE",
    otherName: "航海王 / ワンピース",
    author: "尾田栄一郎",
    company: "東映動画",
    premiereDate: "2022-01-11",
    playStatus: "连载",
    plotType: "搞笑 冒险 热血 励志",
    officialWebsite: "https://www.fujitv.co.jp/b_hp/onepiece/index.html",
    playHeat: "3953",
  });
});
</script>
<style scoped>
.alert {
  text-align: left;
  margin: 10px;
}

.jvm-info {
  width: 100%;
}

.time {
  font-size: 13px;
  color: #999;
}

.bottom {
  margin-top: 13px;
  line-height: 12px;
}

.button {
  padding: 0;
  float: right;
}

.image {
  width: 100%;
  display: block;
}

.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}

.clearfix:after {
  clear: both;
}
</style>
