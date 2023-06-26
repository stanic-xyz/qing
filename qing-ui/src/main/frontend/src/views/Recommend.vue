<script lang="ts" setup>
import { onMounted, ref } from "vue";
import type { Anime } from "@/api/anime";
import axios from "axios";

const animeInfoList = ref<Anime[]>([]);

onMounted(() => {
  const instance = axios.create({
    baseURL: "https://mock.apifox.cn/m1/1876271-0-default",
    timeout: 1000,
    headers: { apifoxToken: "6JQ7DXmRywJ8fsffzsMfLqLcgGMcwuvg" },
  });

  instance
    .post("/animeInfo/v1/findByPage", {
      page: 1,
      pageSize: 87,
      bean: {},
      sorts: {},
    })
    .then(function (response) {
      animeInfoList.value = response.data.result.list;
      console.log(animeInfoList.value);
    })
    .catch(function (error) {
      console.log(error);
    });
  // animeInfoList.value.push({
  //   id: 3,
  //   name: "金好理五候",
  //   instruction: "ex est fugiat",
  //   districtId: 90,
  //   districtName: "持增该山克布",
  //   coverUrl: "http://localhost:8080/img/anime/%E4%BC%A4%E7%89%A9%E8%AF%AD.jpg",
  //   typeId: 29,
  //   typeName: "间大决据手阶",
  //   originalName: "劳特五感长",
  //   otherName: "联统系在置克",
  //   author: "consectetur veniam adipisicing Lorem",
  //   company: "magna ad sint id consequat",
  //   playStatus: "FINISHED",
  //   plotType: "pariatur sint sit",
  //   tags: ["pariatur exercitation irure consequat ad"],
  //   officialWebsite: "labore consectetur dolore ullamco culpa",
  //   playHeat: "sit",
  // });
});
</script>

<template>
  <div id="container">
    <div class="spaceBlock"></div>
    <div class="baseBlock">
      <div class="blockContent">
        <ul class="ul_li_a6">
          <li
            v-for="(animeInfo, index) in animeInfoList"
            :key="index"
            class="anime_icon2"
          >
            <router-link :to="`/anime/${animeInfo.id}`">
              <img
                :alt="animeInfo.name"
                :src="animeInfo.coverUrl"
                :title="animeInfo.name"
                class="anime_icon2_img"
                height="208px"
                referrerpolicy="no-referrer"
                width="150px"
              />
              <span class="anime_icon1_name1">[TV 01-12]</span>
            </router-link>
            <h4 class="anime_icon2_name">
              <router-link :to="`anime/${animeInfo.id}`"
                >{{ animeInfo.name }}
              </router-link>
            </h4>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<style scoped src="../assets/css/play.css"></style>
