<template>
  <!--  生成一个容器-->
  <lay-container fluid>
    <lay-layout>
      <LayBody>
        <div>
          <div>
            <div>每日推荐</div>
            <LayLine theme="red"></LayLine>
          </div>
          <div>
            <ul>
              <li v-for="(anime, index) in animeInfoList" :key="index">
                <AnimeInfo :anime="anime"></AnimeInfo>
              </li>
            </ul>
          </div>
          <lay-empty v-if="animeInfoList.length == 0" description="今日暂无推荐信息"></lay-empty>
          <BlockTitle name="最近播放">最近播放</BlockTitle>
          <div>
            <ul>
              <li v-for="(anime, index) in animeInfoList" :key="index" class="anime_icon1">
                <AnimeInfo :anime="anime"></AnimeInfo>
              </li>
            </ul>
          </div>
        </div>
      </LayBody>
      <LaySide>
        <div>
          <div>周播列表</div>
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
              <li v-for="(anime, index) in animeInfoList" :key="index" class="one_new_anime">
                <router-link :to="`/anime/${anime.id}`">
                  <a class="one_new_anime_name">{{ anime.name }}</a>
                </router-link>
                <a :href="`/anime/${anime.id}'}`" class="one_new_anime_ji">{{ anime.name }}</a>
                <div v-show="true" class="one_anime_new">new!</div>
              </li>
            </ul>
          </div>
          <hr class="hrspace clear" style="width: 95%" />
          <div class="blocktitle">最近更新</div>
          <div class="blockcontent">
            <ul id="anime_update">
              <li v-for="(anime, index) in animeInfoList" :key="index" class="one_new_anime">
                <a class="one_new_anime_name" style="width: 220px">
                  {{ anime.name }}
                </a>
                <span class="anime_update_date asciifont" style="margin-left: 12px">{{ anime.premiereDate }}</span>
              </li>
            </ul>
          </div>
        </div>
      </LaySide>
    </lay-layout>
  </lay-container>
  <!-- 底部 -->
</template>

<script lang="ts" setup>
import { onMounted, ref } from "vue";
import { getAnimeList } from "@/apis/anime";
import AnimeInfo from "@/views/anime/AnimeInfo.vue";
import { LayEmpty, LaySpace } from "@layui/layui-vue";
import BlockTitle from "@/views/common/BlockTitle.vue";
import type { Anime } from "@/apis/anime/types";

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
  console.log("获取动漫卡片数据：开始请求");
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
}
</script>

<style scoped>
@import url("@/assets/css/common/base.css");

.div_left {
  width: 660px;
  display: inline-block;
  margin-right: -8px;
}

.div_right {
  width: 351px;
  float: right;
}

#new_anime_btns {
  margin-bottom: -8px;
}

.new_anime_btn {
  box-sizing: border-box;
  width: 45px;
  padding: 6px 2px 8px 2px;
  margin: 0 -2px 7px 0;
  display: inline-block;
  text-align: center;
  cursor: pointer;

  font-size: 1em;
  border-left: 1px solid transparent;
  border-top: 1px solid transparent;
  border-right: 1px solid transparent;
  border-top-left-radius: 2px;
  border-top-right-radius: 2px;
  background-color: transparent;

  color: #888899;
}

.new_anime_btn:hover,
.new_anime_btn:active {
  border-left: 1px solid #404041;
  border-top: 1px solid #404041;
  border-right: 1px solid #404041;
  background-color: #282828;
  color: #ffffff;
}

.new_anime_btn_current {
  border-left: 1px solid #404041;
  border-top: 1px solid #404041;
  border-right: 1px solid #404041;
  background-color: #282828;
  color: #ffffff;
}

.one_new_anime {
  padding: 3px 0 3px 0;
  width: 100%;
  line-height: 1.4em;
}

.one_new_anime_name {
  width: 156px;
  line-height: 1.4em;
  display: inline-block;
  text-overflow: ellipsis;
  overflow: hidden;
  white-space: nowrap;
  text-decoration-color: #bbbbbc;
}

.one_new_anime_ji {
  width: 108px;
  line-height: 1.4em;
  display: inline-block;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  color: #60b8cc;
  text-decoration-color: #4a94a3;
  margin-left: 8px;
}

.one_anime_new {
  font-size: 0.9em;
  color: #fe0101;
  line-height: 1.4em;
  display: inline-block;
  overflow: hidden;
}

#anime_update {
  font-size: 14.5px;
  background-color: #282828;
  border: 1px solid #404041;
  border-radius: 2px;
  padding: 6px 2px 6px 6px;
}

#comment_update {
  background-color: #282828;
  border: 1px solid #404041;
  border-radius: 2px;
  padding: 6px 2px 6px 6px;
}

.anime_update_date {
  display: inline-block;
  overflow: hidden;
  margin-left: 8px;
  color: #60b8cc;
}

@media only screen and (min-width: 0em) and (max-width: 40em) {
  /* -- */
  .div_right {
    width: 100%;
  }

  .div_left {
    width: 100%;
  }

  .div_right .blocktitle:nth-child(4) {
    display: none;
  }

  .div_right .hrspace:nth-child(3) {
    display: none;
  }

  .div_right .hrspace:nth-child(3) {
    display: none;
  }

  .div_right .blockcontent:nth-child(5) {
    display: none;
  }

  .div_left .blockcontent ul li:nth-child(n + 10) {
    display: none;
  }

  .anime_icon1 {
    width: 30.8% !important;
    margin: 0.6% !important;
    position: relative;
  }

  .anime_icon1 .anime_icon1_img {
    width: 100%;
    height: auto;
    border-radius: 1px;
  }

  .anime_icon1 .anime_icon1_name {
    width: 100%;
  }

  .one_anime_new {
    float: right;
  }

  .one_new_anime_ji {
    float: right;
  }

  #new_anime_btns li {
    width: 13.4%;
  }

  .div_right .blocktitle {
    display: none;
  }
}
</style>
