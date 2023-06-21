<script lang="ts" setup>
import type { PropType } from "vue";
import { onMounted } from "vue";
import type { Anime } from "@/api/anime";

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
      coverUrl:
        "http://localhost:8080/img/anime/%E4%BC%A4%E7%89%A9%E8%AF%AD.jpg",
      typeId: 1,
      typeName: "TV",
      originalName: "ONE PIECE",
      otherName: "航海王 / ワンピース",
      author: "尾田栄一郎",
      company: "東映動画",
      premiereDate: "2022-01-11",
      playStatus: "连载",
      plotType: "搞笑 冒险 热血 励志",
      officialWebsite:
        "http://localhost:8080/img/anime/%E4%BC%A4%E7%89%A9%E8%AF%AD.jpg",
      playHeat: "3953",
    } as Anime,
  },
});

onMounted(() => {
  console.log("发布信息了", props.anime);
});
</script>

<template>
  <div class="cell blockdiff2">
    <router-link :to="`/anime/${props.anime.id}`" class="cell_poster">
      <img
        :src="props.anime.coverUrl"
        alt="发布信息"
        class="anime_icon1_img"
        height="208px"
        referrerpolicy="no-referrer"
        width="150px"
      />
      <span class="newname">第120话</span></router-link
    >
    <div class="cell_imform">
      <div>
        <router-link :to="`/anime/${props.anime.id}`" class="cell_imform_name"
          >{{ props.anime.name }}
        </router-link>
      </div>
      <div class="cell_imform_kvs">
        <div class="cell_imform_kv">
          <span class="cell_imform_tag">动画种类：</span>
          <span class="cell_imform_value">{{ props.anime.typeName }}</span>
        </div>
        <div class="cell_imform_kv">
          <span class="cell_imform_tag">原版名称：</span>
          <span class="cell_imform_value">{{ props.anime.originalName }}</span>
        </div>
        <div class="cell_imform_kv">
          <span class="cell_imform_tag">其他名称：</span>
          <span class="cell_imform_value">{{ props.anime.otherName }}</span>
        </div>
        <br />
        <div class="cell_imform_kv">
          <span class="cell_imform_tag">首播时间：</span>
          <span class="cell_imform_value">{{ props.anime.premiereDate }}</span>
        </div>
        <div class="cell_imform_kv">
          <span class="cell_imform_tag">播放状态：</span>
          <span class="cell_imform_value">{{ props.anime.playStatus }}</span>
        </div>
        <div class="cell_imform_kv">
          <span class="cell_imform_tag">原作：</span>
          <span class="cell_imform_value">{{ props.anime.author }}</span>
        </div>
        <br />
        <div class="cell_imform_kv">
          <span class="cell_imform_tag">剧情类型：</span>
          <span class="cell_imform_value">{{ props.anime.plotType }}</span>
        </div>
        <div class="cell_imform_kv">
          <span class="cell_imform_tag">制作公司：</span>
          <span class="cell_imform_value">{{ props.anime.company }}</span>
        </div>
        <div class="cell_imform_kv cell_imform_kv_desc">
          <span class="cell_imform_tag">简介</span>
          <div class="cell_imform_desc">
            {{ props.anime.instruction }}
          </div>
        </div>
      </div>
      <div class="cell_imform_btns">
        <router-link
          :to="`/play/${props.anime.id}`"
          class="nbutton2 cell_res_button"
          >资源详情
        </router-link>
        <router-link
          :to="`play/${props.anime.id}`"
          class="nbutton2 cell_res_button"
          >在线播放
        </router-link>
      </div>
    </div>
  </div>
</template>

<style scoped src="../../assets/css/catalog.css"></style>
