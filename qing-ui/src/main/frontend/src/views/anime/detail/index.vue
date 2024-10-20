<style lang="scss">
.content-wrapper {
  background-color: rgb(33, 37, 41);
  margin: 1rem 0;
  border-radius: 10px;
  display: flex;
  flex-direction: row;
  padding: 1rem;
  color: white;

  .div-left {
    display: flex;
    flex-direction: column;
    width: 256px;

    .cover {
      width: 100%;
      height: 100%;
      align-items: center;
      text-align: center;

      img {
        width: 256px;
      }
    }

    .summary {
      height: 2rem;
      justify-content: space-between;
      display: flex !important;
      background-color: #202020;
      flex: 1;
      width: 100%;
      margin: 1rem 0;

      .summary-item {
        border: 1px solid rgba(255, 255, 255, 0.1);
        padding: 8px 0;
        flex: 1 1 auto !important;
        text-align: center;

        .fire {
          color: red;
        }
      }
    }

    .detail-info {
      background-color: rgb(32, 32, 32);
      border: 1px solid rgba(255, 255, 255, 0.1);
      padding: 0 1rem;

      .detail-info-title {
        color: red;
        margin: 1rem 0;
      }

      .detail-info-content {
        color: #ffffff;

        .detail-info-content-item {
          height: 1rem;
          margin-bottom: 1rem;

          .detail-info-content-item-tag {
            color: gray;
            display: inline-block;
            width: 80px;
          }

          .detail-info-content-item-value {
            display: inline-block;
            color: whitesmoke;
          }
        }
      }
    }
  }

  .div-right {
    margin-left: 1rem;
    display: flex;
    flex-direction: column;

    .description {
      padding-bottom: 1rem;

      .detail-info-name {
        h2 {
          color: #d0e0f0;
        }
      }

      .detail-info-divider {
        width: 100%;
        color: rgba(25, 25, 25, 0.5);
      }
    }

    .playlist-wrapper {
      width: 100%;

      .playlist-header {
        width: 100%;
        padding-top: 1rem;
        display: flex;
        flex-direction: row;
        justify-content: space-between;
        align-items: center;
        align-content: center;
        font-size: 1rem;

        .playlist-title-icon {
          color: #dc3545;
        }

        .playlist-title {
          margin-left: 0.5rem;
          color: #dc3545;
        }

        .playlist-notice {
          color: darkgray;
        }
      }
    }

    .playlist-content {
      width: 100%;

      .playlist-content-tab {
        width: 100%;
        display: flex;

        .playlist-content-tab-item {
          background-color: rgb(33, 37, 41);
          line-height: 1rem !important;
          text-align: center;
          padding: 0.5rem;
          color: rgba(255, 255, 255, 0.55);
          border-radius: 10px 10px 0 0;
          width: 100px;
          cursor: pointer;

          &:hover {
            color: white;
          }

          .playlist-title-icon {
            color: darkred;
          }
        }

        .selected {
          background-color: rgb(55, 55, 56);
        }
      }

      .playlist-content-content {
        width: 100%;
        min-height: 240px;
        max-height: 500px;
        background-color: rgb(55, 55, 56);
        padding: 1rem;

        .playlist-content-playlist-episode-item {
          display: inline-block;
          border: 1px solid gray;
          border-radius: 6px;
          height: 40px;
          width: 11.111%;
          line-height: 40px;
          text-align: center;
          overflow: hidden;
          cursor: pointer;
          margin-bottom: 2px;
          padding: 0 2px;

          &:hover {
            background-color: #dc3545;
          }
        }
      }
    }
  }
}
</style>

<template>
  <div class="x-container content-wrapper">
    <div class="div-left">
      <div class="cover">
        <img :alt="data.anime.name" class="poster" height="356px" referrerpolicy="no-referrer" src="https://cdn.aqdstatic.com:966/age/20010004.jpg" width="256px" />
      </div>
      <div class="summary">
        <div class="summary-item">
          <lay-icon type="layui-icon-fire" class="fire"></lay-icon>
          20W
        </div>
        <div class="summary-item">
          <lay-icon type="layui-icon-dialogue" class="fire"></lay-icon>
          494
        </div>
        <div class="summary-item">
          <lay-icon type="layui-icon-heart" class="fire"></lay-icon>
          2,363
        </div>
      </div>

      <div class="detail-info">
        <div class="detail-info-title">基本信息</div>
        <ul class="detail-info-content">
          <li class="detail-info-content-item">
            <span class="detail-info-content-item-tag">地区：</span>
            <span class="detail-info-content-item-value">{{ data.anime.name }}</span>
          </li>
          <li class="detail-info-content-item">
            <span class="detail-info-content-item-tag">动画种类：</span>
            <span class="detail-info-content-item-value">{{ data.anime.typeName }}</span>
          </li>
          <li class="detail-info-content-item">
            <span class="detail-info-content-item-tag">动画名称：</span>
            <span class="detail-info-content-item-value">{{ data.anime.name }}</span>
          </li>
          <li class="detail-info-content-item">
            <span class="detail-info-content-item-tag">原版名称：</span>
            <span class="detail-info-content-item-value">{{ data.anime.originalName }}</span>
          </li>
          <li class="detail-info-content-item">
            <span class="detail-info-content-item-tag">作者：</span>
            <span class="detail-info-content-item-value">{{ data.anime.author }}</span>
          </li>
          <li class="detail-info-content-item">
            <span class="detail-info-content-item-tag">制作公司：</span>
            <span class="detail-info-content-item-value">{{ data.anime.companyName }}</span>
          </li>
          <li class="detail-info-content-item">
            <span class="detail-info-content-item-tag">首播时间：</span>
            <span class="detail-info-content-item-value">{{ data.anime.premiereDate }}</span>
          </li>
          <li class="detail-info-content-item">
            <span class="detail-info-content-item-tag">播放状态：</span>
            <span class="detail-info-content-item-value">{{ data.anime.playStatus }}</span>
          </li>
          <li class="detail-info-content-item">
            <span class="detail-info-content-item-tag">剧情类型：</span>
            <span class="detail-info-content-item-value">{{ data.anime.plotType }}</span>
          </li>
          <li class="detail-info-content-item">
            <span class="detail-info-content-item-tag">标签：</span>
            <span class="detail-info-content-item-value">{{ data.anime.tagIds }}</span>
          </li>
        </ul>
      </div>
    </div>
    <div class="div-right">
      <div class="description">
        <div class="detail-info-name">
          <h2>
            {{ data.anime.name }}
          </h2>
        </div>
        <hr class="detail-info-divider" />
        <div class="baseblock">
          <div class="blockcontent">
            <div class="detail_imform_desc_pre">
              <p>{{ data.anime.instruction }}</p>
            </div>
          </div>
        </div>
      </div>
      <div class="playlist-wrapper">
        <div class="playlist-header">
          <div>
            <span>
              <lay-icon type="layui-icon-play" class="playlist-title-icon"></lay-icon>
              <span class="playlist-title">在线播放</span>
            </span>
          </div>
          <small class="playlist-notice">视频如果未正常播放或者卡顿，请切换播放源，优先选择 VIP 播放源!</small>
        </div>
        <hr />
        <div class="playlist-content">
          <div class="playlist-content-tab">
            <div class="playlist-content-tab-item" :class="{ selected: selectTab == list.id }" :title="list.name" id="1" v-for="(list, index) of data.anime.playLists" :key="index" @click="choseTab(list.id)">
              <lay-icon type="layui-icon-gitee" class="playlist-title-icon"></lay-icon>
              {{ list.name }}
            </div>
          </div>

          <div class="playlist-content-content" v-for="(list, index) in data.anime.playLists" :key="index" v-show="selectTab === list.id">
            <ul>
              <li class="playlist-content-playlist-episode-item" v-for="(episode, episodeIndex) in list.episodeList" :key="episodeIndex">
                {{ episode.name }}
                <div class="playing">播放中</div>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { onMounted, reactive, ref } from "vue";
import { findDetailById } from "@/apis/anime";
import { useRoute, useRouter } from "vue-router";
import type { AnimeDetail } from "@/apis/anime/types";

const selectTab = ref(1);

const data = reactive({
  activeIndex: "1",
  activeIndex2: "1",
  currentDate: new Date(),
  menuIndex: 0,
  time: "2020年01月15日 21时50分19秒",
  loading: true,
  anime: {} as AnimeDetail,
  reportTypes: [
    {
      id: "1",
      name: "反馈类型1",
    },
  ],
});

onMounted(() => {
  const id = useRoute().params.animeId;
  console.log("加载了id", id);
  if (typeof id === "string") {
    findDetailById(Number.parseInt(id))
      .then((response) => {
        console.log("获取到了结果了", response.result);
        data.anime = response.result;
        if (response.result.playLists.length > 0) {
          selectTab.value = response.result.playLists.at(0).id;
        }
      })
      .catch((error) => {
        console.log("为查询到动漫信息", error.code, error.response.data.message);
      });
  } else {
    // 参数错误
    useRouter().push("/error/paramError");
  }
});

const currentPlayListId = ref(0);
const isCommentBoard = ref(false);
const chooseReportTypes = ref([]);
const reportDetail = ref("");
const episodeList = ref<any>([
  {
    id: "12123",
    name: "测试名称",
  },
]);
const commentList = reactive([
  {
    createTime: {
      nano: 0,
      year: 2021,
      monthValue: 7,
      dayOfMonth: 28,
      hour: 23,
      minute: 21,
      second: 40,
      month: "JULY",
      dayOfWeek: "WEDNESDAY",
      dayOfYear: 209,
      chronology: {
        id: "ISO",
        calendarType: "iso8601",
      },
    },
    updateTime: {
      nano: 0,
      year: 2021,
      monthValue: 7,
      dayOfMonth: 28,
      hour: 23,
      minute: 21,
      second: 39,
      month: "JULY",
      dayOfWeek: "WEDNESDAY",
      dayOfYear: 209,
      chronology: {
        id: "ISO",
        calendarType: "iso8601",
      },
    },
    searchValue: "",
    createBy: "",
    updateBy: "",
    remark: "",
    id: "1420404091520241666",
    cid: 20000001,
    username: "游客",
    content: "啦啦啦",
    ipAddress: null,
  },
]);

function next(animeId: any) {
  console.log(animeId);
}

function nextPage() {
  console.log("翻页到下一页评论");
}

function choseTab(playListId: number) {
  this.selectTab = playListId;
}

function changeRecommendBoard() {
  isCommentBoard.value = !isCommentBoard.value;
  console.log(isCommentBoard.value);
}

function sendReport() {
  console.log("发起了举报");
  alert(reportDetail.value);
}
</script>
