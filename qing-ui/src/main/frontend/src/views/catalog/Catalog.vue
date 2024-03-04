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
    <lay-page v-model="currentPage" :limit="limit" :show-page="true" :total="total"></lay-page>
  </div>
</template>

<script lang="ts" setup>
import {onMounted, ref} from "vue";
import CatalogItem from "@/views/catalog/CatalogItem.vue";
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
const data = ref({
  activeIndex: "1",
  activeIndex2: "1",
  currentDate: new Date(),
  time: "2020年01月15日 21时50分19秒",
  loading: true,
  videoUrl: "https://1251316161.vod2.myqcloud.com/007a649dvodcq1251316161/75b32a815285890809903778875/5cAXY7aMUEQA.mp4",
  totalCount: 10,
  regionNames: ["全部", "日本", "中国", "欧美"],
  genreNames: ["全部", "TV", "剧场版", "OVA"],
  letterNames: ["全部", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"],
  yearNames: ["全部", "2020", "2019", "2018", "2017", "2016", "2015", "2014", "2013", "2012", "2011", "2010", "2009", "2008", "2007", "2006", "2005", "2004", "2003", "2002", "2001", "2000"],
  seasonNames: ["全部", "1", "4", "7", "10"],
  statusNames: ["全部", "连载", "完结", "未播放"],
  labelNames: ["全部", "搞笑", "运动", "励志", "热血", "战斗", "竞技", "校园", "青春", "爱情", "冒险", "后宫", "百合", "治愈", "萝莉", "魔法", "悬疑", "推理", "奇幻", "科幻", "游戏", "神魔", "恐怖", "血腥", "机战", "战争", "犯罪", "历史", "社会", "职场", "剧情", "伪娘", "耽美", "童年", "教育", "亲子", "真人", "歌舞", "肉番", "美少女", "轻小说", "吸血鬼", "乙女向", "泡面番", "欢乐向"],
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
