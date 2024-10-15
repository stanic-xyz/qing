<style lang="scss">
.content-wrapper {
  background-color: rgb(33, 37, 41);
  margin-top: 1rem;
  border-radius: 10px;
  display: flex;
  flex-direction: row;
  padding: 1rem;
  color: white;

  .div-left {
    display: flex;
    flex-direction: column;
    width: 30%;

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
      display: flex;
      align-content: center;
      justify-content: space-between;
      margin: 1rem;
    }

    .detail-info {
      background-color: #0b2e13;
    }
  }

  .div-right {
    display: flex;
    flex-direction: column;

    .description {
      padding: 1rem 0;

      .detail-info-name {
        h2 {
          color: #d0e0f0;
        }
      }

      .detail-info-divider {
        width: 100%;
        color: rgba(153, 153, 153, 0.5);
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
  }
}
</style>

<template>
  <div class="x-container content-wrapper">
    <div class="div-left">
      <div class="cover">
        <img :alt="data.anime.name" class="poster" height="356px" referrerpolicy="no-referrer" src="https://cdn.aqdstatic.com:966/age/20010004.jpg" width="256px"/>
      </div>
      <div class="summary">
        <button>测试信息</button>
        <button>测试信息</button>
        <button>测试信息</button>
      </div>

      <div class="detail-info">
        <div class="blocktitle detail_title1">基本信息：</div>
        <ul class="blockcontent">
          <li class="detail_imform_kv">
            <span class="detail_imform_tag">地区：</span>
            <span class="detail_imform_value">{{ data.anime.name }}</span>
            <a class="detail_imform_show_full" href="javascript:detail_show_full();">&lt;&lt;展开</a>
          </li>
          <li class="detail_imform_kv">
            <span class="detail_imform_tag">动画种类：</span>
            <span class="detail_imform_value">{{ data.anime.typeName }}</span>
          </li>
          <li class="detail_imform_kv">
            <span class="detail_imform_tag">动画名称：</span>
            <span class="detail_imform_value">{{ data.anime.name }}</span>
          </li>
          <li class="detail_imform_kv">
            <span class="detail_imform_tag">原版名称：</span>
            <span class="detail_imform_value">{{ data.anime.originalName }}</span>
          </li>
          <li class="detail_imform_kv">
            <span class="detail_imform_tag">作者：</span>
            <span class="detail_imform_value">{{ data.anime.author }}</span>
          </li>
          <li class="detail_imform_kv">
            <span class="detail_imform_tag">制作公司：</span>
            <span class="detail_imform_value">{{ data.anime.companyName }}</span>
          </li>
          <li class="detail_imform_kv">
            <span class="detail_imform_tag">首播时间：</span>
            <span class="detail_imform_value">{{ data.anime.premiereDate }}</span>
          </li>
          <li class="detail_imform_kv">
            <span class="detail_imform_tag">播放状态：</span>
            <span class="detail_imform_value">{{ data.anime.playStatus }}</span>
          </li>
          <li class="detail_imform_kv">
            <span class="detail_imform_tag">剧情类型：</span>
            <span class="detail_imform_value">{{ data.anime.plotType }}</span>
          </li>
          <li class="detail_imform_kv">
            <span class="detail_imform_tag">标签：</span>
            <span class="detail_imform_value">{{ data.anime.tagIds }}</span>
          </li>
          <li class="detail_imform_kv">
            <span class="detail_imform_tag">官方网站：</span>
            <span class="detail_imform_value">
              <a :href="data?.anime?.officialWebsite" target="_blank">{{ data?.anime?.officialWebsite }}</a>
            </span>
          </li>
        </ul>
      </div>
      <div class="baseblock">
        <div class="blockcontent">
          <div class="baseblock2">
            <div class="blocktitle detail_title1">相关动画：</div>
            <ul class="blockcontent">
              <li class="relates_series">
                <router-link :to="'/anime/20170004'">为美好的世界献上祝福！ 第二季</router-link>
              </li>

              <li class="relates_series">
                <a href="/anime/20160084">为美好的世界献上祝福！OAD</a>
              </li>

              <li class="relates_series">
                <a href="/anime/20180187">为美好的世界献上祝福！红传说</a>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </div>
    <div class="div-right">
      <div class="description">
        <div class="detail-info-name">
          <h2>
            {{ data.anime.name }}
          </h2>
        </div>
        <hr class="detail-info-divider"/>
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
        <hr/>

        <div class="playlist">
          <lay-tab v-model="selectTab" type="button">
            <lay-tab-item :title="list.name" v-if="true" id="1" v-for="(list, index) in data.anime.playLists" :key="index">
              <lay-button type="normal" v-for="(episodeInfo, episodeIndex) in list.episodeList" :key="episodeIndex">
                <router-link :title="episodeInfo.name" :to="`/anime/${data.anime.id}/play/{listId}/{episodeId}(animeId=${data.anime.id}`" target="_self">{{ episodeInfo.name }}</router-link>
              </lay-button>
            </lay-tab-item>
          </lay-tab>
        </div>
      </div>

      <div class="blockline1" style="margin: 8px"></div>
      <div id="wangpan-div" class="baseblock">
        <div class="blocktitle">网盘资源：</div>
        <div class="blockcontent">
          <span class="res_links">
            <a class="res_links_a" href="/link/20160083/0" target="_blank">[TV 01-10+OVA 720P]</a>
            <span class="res_links_pswd_tag">(提取码:</span>
            <span class="res_links_pswd">fe5e)</span></span
          >
          <br/>
        </div>
      </div>

      <div class="blockline1" style="margin: 8px"></div>
      <div class="baseblock">
        <div class="blocktitle">反馈：</div>
        <div class="report_div">
          <div id="report_form">
            <div>
              <input id="report_aid" v-model="data.anime.id" name="cid" type="hidden"/>
              <label v-for="(type, index) in data.reportTypes" :key="index"> <input v-model="chooseReportTypes" name="link_invalid" type="radio" v-bind:value="type.name"/>{{ type.name }}</label>
              <br/>
              <label>
                {{ chooseReportTypes }}
                <br/>
                <textarea v-model="reportDetail" autocapitalize="off" autocomplete="off" name="detail" placeholder="请告诉我们详细情况~" spellcheck="true"></textarea>
              </label>
              <br/>
              <button class="nbutton" value="提交" @click="sendReport">提交</button>
            </div>
          </div>
        </div>
      </div>

      <div class="blockline1" style="margin: 8px"></div>
      <div class="baseblock">
        <div class="blockcontent">
          <!-- switch -->
          <div style="padding-left: 8px">
            <button :class="{ switchButton_current: !isCommentBoard }" class="switchbtn" @onclick="changeRecommendBoard">相关推荐</button>
            <button :class="{ switchButton_current: isCommentBoard }" class="switchbtn" @onclick="changeRecommendBoard">留言板</button>
          </div>
          <div id="recommend_block" class="switchBlock">
            <ul class="ul_li_a4">
              <li v-for="({ id, name }, index) in []" :key="index" class="anime_icon1">
                <router-link :to="`/anime/${id}`"><img alt="暂无" class="anime_icon1_img" height="205" referrerpolicy="no-referrer" src="../../../assets/img/anime/伤物语_small.jpg" width="148"/></router-link>
                <router-link :to="`/anime/${id}`">
                  <div class="anime_icon1_name">{{ name }}</div>
                </router-link>
              </li>
            </ul>
          </div>
          <div id="comments_block" class="switchBlock">
            <form id="comment_form" action="javascript:void(0)" method="GET">
              <div>
                <input id="comment_id" name="cid" type="hidden" value="${data?.anime?.id}"/>
                <label for="comment_content"></label>
                <textarea id="comment_content" autocapitalize="off" autocomplete="off" csrf_token="kqYFPKw9DOko88jkfLKSATbDkz6ipQBD9pYXLohUtVGXpMhQPdkP7lyYGDwiNifm" maxlength="255" name="comment_content" placeholder="说点什么吧" spellcheck="false" tid="20180132" wrap="SOFT"></textarea>
                <label class="comment_imform_tag" for="comment_user">昵称：</label>
                <input id="comment_user" autocapitalize="off" autocomplete="off" name="comment_user" placeholder="名字" readonly spellcheck="false" value="游客"/>
                <input class="nbutton" name="" type="submit"/>
              </div>
            </form>

            <!-- 评论列表 -->
            <ul id="comment_list" style="margin-top: 16px">
              <li v-for="(comment, index) in commentList" :key="index" class="comment">
                <hr class="hrspace2"/>
                <div class="comment_cell_user">
                  {{ comment.username }}
                </div>
                <div class="comment_cell_content">
                  <div>{{ comment.content }}</div>
                  <div class="comment_cell_time asciifont">
                    {{ comment.createTime }}
                  </div>
                </div>
              </li>
              <hr class="hrspace2"/>
              <div id="current_comment_page"></div>
            </ul>

            <!-- 评论翻页 -->
            <div class="spaceblock1"></div>
            <ul class="comment_page">
              <li>
                <button class="${data?.comments?.current == 1 ?'pbutton asciifont pbutton_current':'pbutton asciifont'}" data-cid="${data?.anime?.id}" data-pages="${data?.comments?.getPages()}" data-size="${data?.comments?.size}" href="javascript:void(0)" @onclick="nextPage">首页</button>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import {onMounted, reactive, ref} from "vue";
import {findDetailById} from "@/apis/anime";
import {useRoute, useRouter} from "vue-router";
import type {AnimeDetail} from "@/apis/anime/types";

const selectTab = ref("1");

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

function changeRecommendBoard() {
  isCommentBoard.value = !isCommentBoard.value;
  console.log(isCommentBoard.value);
}

function sendReport() {
  console.log("发起了举报");
  alert(reportDetail.value);
}
</script>
