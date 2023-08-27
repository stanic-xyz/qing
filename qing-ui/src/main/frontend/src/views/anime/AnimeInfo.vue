<script lang="ts" setup>
import type { PropType } from "vue";
import { onMounted } from "vue";
import type { Anime } from "@/api/anime/types";

onMounted(() => {
  console.log("动漫详情加载成功");
});

const props = defineProps({
  anime: {
    type: Object as PropType<Anime>,
    required: false,
    name: {
      type: String,
      default: "动漫名称",
    },
    default: {
      id: 20000001,
      name: "海贼王",
      instruction:
        "电视动画《航海王》改编自尾田荣一郎创作的同名长篇少年漫画，动画由东映动画制作。 海贼王哥尔·D·罗杰在临死前曾留下了关于其毕生的财富“One Piece”的消息，由此引得群雄并起，众海盗们为了这笔传说中的巨额财富展开争夺，各种势力、政权不断交替，整个世界进入了动荡混乱的“大海贼时代”。 生长在东海某小村庄的路飞受到海贼香克斯的精神指引，决定成为一名出色的海盗。为了达成这个目标，并找到万众瞩目的One Piece，路飞踏上艰苦的旅程。一路上他遇到了无数磨难，也结识了索隆、娜美、山治、乌索普、罗宾等一众性格各异的好友。他们携手一同展开充满传奇色彩的大冒险。",
      districtId: 1,
      districtName: "日本",
      coverUrl: "",
      typeId: 1,
      typeName: "TV",
      originalName: "ONE PIECE",
      otherName: "航海王 / ワンピース",
      author: "尾田栄一郎",
      company: "東映動画",
      premiereDate: "2022-01-11",
      playStatus: "连载",
      plotType: "搞笑 冒险 热血 励志",
      officialWebsite: "",
      playHeat: "3953",
    } as Anime,
  },
});

onMounted(() => {
  console.debug("单个动漫信息加载成功", props.anime);
});
</script>

<template>
  <div class="card-container" style="width: 130px; margin: 15px; padding: 5px">
    <router-link :to="`13123-${anime.id}`">
      <div class="video_item">
        <div class="video_item--image position-relative">
          <img
            referrerpolicy="no-referrer"
            :src="anime.coverUrl"
            data-original="https://cdn.aqdstatic.com:966/age/20220255.jpg"
            class="video_thumbs lazyload d-block w-100"
            style="display: block; width: 120px"
            :alt="anime.name"
          /><span class="video_item--info rounded-1 text-truncate">第08集</span>
        </div>
        <div
          class="video_item-title text-truncate text-center py-2"
          style="color: black"
        >
          <div class="link-light text-decoration-none stretched-link">
            {{ anime.name }}
          </div>
        </div>
      </div>
    </router-link>
  </div>
</template>

<style scoped>
.card-container {
  background: whitesmoke;
  padding: 0;
  width: auto;
}
</style>
