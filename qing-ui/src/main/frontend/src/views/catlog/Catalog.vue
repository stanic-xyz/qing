<template>
  <div id="container">
    <div class="baseblock">
      <div class="blockcontent" style="position: relative">
        <span class="asciifont result_count">{{ 1 }}记录</span>
      </div>
    </div>
    <div v-for="(anime, index) in animeInfoList" :key="index">
      <CatalogItem :anime="anime"></CatalogItem>
    </div>
    <div class="baseblock">
      <div class="blockcontent">
        <span class="asciifont result_count">{{ 1 }}记录</span>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { onMounted, ref } from "vue";
import CatalogItem from "@/views/catlog/CatalogItem.vue";
import type { Anime } from "@/api/anime";

const animeInfoList = ref<Anime[]>([]);

onMounted(() => {
  for (let i = 0; i < 10; i++) {
    animeInfoList.value.push({
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
    });
  }
});
const data = ref({
  activeIndex: "1",
  activeIndex2: "1",
  currentDate: new Date(),
  time: "2020年01月15日 21时50分19秒",
  loading: true,
  videoUrl:
    "https://1251316161.vod2.myqcloud.com/007a649dvodcq1251316161/75b32a815285890809903778875/5cAXY7aMUEQA.mp4",
  totalCount: 10,
  regionNames: ["全部", "日本", "中国", "欧美"],
  genreNames: ["全部", "TV", "剧场版", "OVA"],
  letterNames: [
    "全部",
    "A",
    "B",
    "C",
    "D",
    "E",
    "F",
    "G",
    "H",
    "I",
    "J",
    "K",
    "L",
    "M",
    "N",
    "O",
    "P",
    "Q",
    "R",
    "S",
    "T",
    "U",
    "V",
    "W",
    "X",
    "Y",
    "Z",
  ],
  yearNames: [
    "全部",
    "2020",
    "2019",
    "2018",
    "2017",
    "2016",
    "2015",
    "2014",
    "2013",
    "2012",
    "2011",
    "2010",
    "2009",
    "2008",
    "2007",
    "2006",
    "2005",
    "2004",
    "2003",
    "2002",
    "2001",
    "2000",
  ],
  seasonNames: ["全部", "1", "4", "7", "10"],
  statusNames: ["全部", "连载", "完结", "未播放"],
  labelNames: [
    "全部",
    "搞笑",
    "运动",
    "励志",
    "热血",
    "战斗",
    "竞技",
    "校园",
    "青春",
    "爱情",
    "冒险",
    "后宫",
    "百合",
    "治愈",
    "萝莉",
    "魔法",
    "悬疑",
    "推理",
    "奇幻",
    "科幻",
    "游戏",
    "神魔",
    "恐怖",
    "血腥",
    "机战",
    "战争",
    "犯罪",
    "历史",
    "社会",
    "职场",
    "剧情",
    "伪娘",
    "耽美",
    "童年",
    "教育",
    "亲子",
    "真人",
    "歌舞",
    "肉番",
    "美少女",
    "轻小说",
    "吸血鬼",
    "乙女向",
    "泡面番",
    "欢乐向",
  ],
  resourceNames: ["全部", "BDRIP", "AGE-RIP"],
  orderNames: ["更新时间", "名称", "点击量"],
  queryObject: {
    region: "全部",
    version: "全部",
    letter: "全部",
    year: "全部",
    season: "全部",
    label: "全部",
    status: "全部",
    order: "全部",
    resource: "全部",
  },
  message: "test",
});
</script>

<style scoped src="../../assets/css/catalog.css"></style>
