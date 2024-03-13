<template>
  <div id="container">
    <div class="spaceBlock"></div>
    <div class="baseBlock">
      <div class="blockContent">
        <div class="spaceBlock"></div>
        <ul class="ul_li_a6">
          <li v-for="(anime, index) in animeList" :key="index" class="anime_icon2">
            <router-link :to="`/anime/${anime.id}`">
              <img :alt="anime.name" :src="anime.coverUrl" :title="anime.name" class="anime_icon2_img" height="208px" referrerpolicy="no-referrer" width="150px" />
              <span class="anime_icon1_name1">02:00 第10话</span>
            </router-link>
            <h4 class="anime_icon2_name">
              <router-link :to="`/anime/${anime.id}`">
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
import type { Anime } from "@/apis/anime/types";
import { page } from "@/apis/anime";

const animeList = ref<Anime[]>([]);

const data = ref({
  activeIndex: "1",
  activeIndex2: "1",
  currentDate: new Date(),
  time: "2020年01月15日 21时50分19秒",
  loading: true,
});

onMounted(() => {
  page({
    pageSize: 10,
    page: 0,
    sorts: [
      {
        column: "updatedAt",
        direction: "desc",
      },
    ],
  })
    .then(function (response) {
      const animeDataList = response.result.list || [];
      console.log("获取到动漫信息内容", animeDataList);
      animeDataList.forEach((anime) => {
        animeList.value.push(anime);
      });
    })
    .catch(function (error) {
      console.log(error);
    });
});
</script>
<style scoped lang="scss">
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

.clearfix:after {
  clear: both;
}
</style>
