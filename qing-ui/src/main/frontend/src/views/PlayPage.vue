<script lang="ts" setup>
import {onMounted, reactive, ref} from "vue";
import MyDPlayer from "@/components/MyDPlayer.vue";
import type {Anime} from "@/apis/anime/types";
import {findDetailById} from "@/apis/anime";

const el = ref();
const anime = ref<Anime>({} as Anime);

const data = reactive({
  activeIndex: "1",
  activeIndex2: "1",
  currentDate: new Date(),
  time: "2020年01月15日 21时50分19秒",
  loading: true,
  videoUrl: "https://anime-1255705827.cos.ap-guangzhou.myqcloud.com/test.mp4",
});

const array = ref([1, 2, 3, 4, 5, 6, 7]);

const playList = ref([
  {
    id: 1,
    name: "播放列表1",
    episodeList: [
      {
        id: 1,
        name: "测试名称",
      },
    ],
  },
]);
const currentListId = ref(0);
const currentEpisodeId = ref(0);
onMounted(() => {
  el.value; // <div>
  anime.value = {} as Anime;
  findDetailById(anime.value.id).then((res) => {
    console.log(res);
  });
});

function changeList(listId: any) {
  currentListId.value = listId;
}
</script>
<template>
  <div id="container">
    <div class="baseblock">
      <div class="blocktitle">
        <h4 id="detailname">
          <router-link :to="`/anime/${anime.id}`">{{ anime.name }}</router-link>
        </h4>
      </div>
      <div class="line"></div>
      <div class="blockcontent">
        <table>
          <tbody>
            <tr>
              <td>
                <router-link :to="`'/anime/${anime.id}`">
                  <img id="play_poster_img" alt="番剧剧照" height="260" referrerpolicy="no-referrer" src="../assets/img/anime/伤物语.jpg" width="187"/>
                </router-link>
              </td>
              <td>
                <ul id="play_imform">
                  <li class="play_imform_kv">
                    <span class="play_imform_tag">地区：</span>
                    <span class="play_imform_val">{{ anime.districtName }}</span>
                  </li>
                  <li class="play_imform_kv">
                    <span class="play_imform_tag">动画种类：</span>
                    <span class="play_imform_val">{{ anime.typeName }}</span>
                  </li>
                  <li class="play_imform_kv">
                    <span class="play_imform_tag">原作：</span>
                    <span class="play_imform_val">{{ anime.author }}</span>
                  </li>
                  <li class="play_imform_kv">
                    <span class="play_imform_tag">原版名称：</span>
                    <span class="play_imform_val">{{ anime.originalName }}</span>
                  </li>
                  <li class="play_imform_kv">
                    <span class="play_imform_tag">其它名称：</span>
                    <span class="play_imform_val">{{ anime.otherName }}</span>
                  </li>
                  <li class="play_imform_kv">
                    <span class="play_imform_tag">制作公司：</span>
                    <span class="play_imform_val">{{ anime.companyName }}</span>
                  </li>
                  <li class="play_imform_kv">
                    <span class="play_imform_tag">首播时间：</span>
                    <span class="play_imform_val">{{ anime.premiereDate }}</span>
                  </li>
                  <li class="play_imform_kv">
                    <span class="play_imform_tag">播放状态：</span>
                    <span class="play_imform_val">{{ anime.playStatus }}</span>
                  </li>
                  <li class="play_imform_kv">
                    <span class="play_imform_tag">剧情类型：</span>
                    <span class="play_imform_val">{{ anime.tagIds }}</span>
                  </li>
                  <li class="play_imform_kv">
                    <span class="play_imform_tag">更新时间：</span>
                    <span class="play_imform_val">{{ anime.premiereDate }}</span>
                  </li>
                  <li class="play_imform_kv">
                    <span class="play_imform_tag">官方网站：</span>
                    <span class="play_imform_val">{{ anime.officialWebsite }}</span>
                  </li>
                </ul>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <div class="spaceblock1"></div>
    <div class="baseblock">
      <div class="blocktitle">简介：</div>
      <div class="line"></div>
      <div class="blockcontent">
        <div class="play_desc">
          <p>{{ anime.instruction }}</p>
        </div>
      </div>
    </div>

    <div class="baseblock">
      <div class="blockcontent">
        <div id="pp" class="baseblock2">
          <div id="playlist-div" class="baseblock">
            <div class="blocktitle">在线播放：</div>
            <ul id="menu0" class="menu0">
              <li v-for="(listInfo, index) in playList" :key="index" :class="{ on: (currentListId = listInfo.id) }" data-index="${iterStat.index}" style="display: block" @onclick="changeList(listInfo.id)">
                {{ listInfo.name }}
              </li>
            </ul>
            <div id="main0" class="main0">
              <div v-for="(listinfo, index) in playList" v-show="currentListId === index" :key="index" class="movurl" style="display: block">
                <ul>
                  <li v-for="(episode, episodeIndex) in listinfo.episodeList" :key="episodeIndex">
                    <router-link :title="episode.name" :to="`/play/${episode.id}`" class="on" target="_self">
                      {{ episode.name }}
                    </router-link>
                  </li>
                </ul>
              </div>
            </div>
          </div>
          <div id="ageframeblock" class="content">
            <div id="ageframediv" style="width: 980px; height: 551px">
              <my-d-player :likes="1" url="https://anime-1255705827.cos.ap-guangzhou.myqcloud.com/test.mp4"></my-d-player>
            </div>
          </div>
        </div>
      </div>

      <div class="spaceblock1"></div>
      <div class="baseblock">
        <div class="blocktitle">看过《{{ anime.name }}》的人还看过：</div>
        <div class="line"></div>
        <div class="blockcontent">
          <ul class="ul_li_a8">
            <li v-for="(item, index) in array" :key="index" class="anime_icon1">
              <a href="/anime/20170042"> <img class="anime_icon1_img" height="205" referrerpolicy="no-referrer" src="../assets/img/anime/伤物语_small.jpg" width="148"/></a>
              <a href="/anime/20170042">
                <div class="anime_icon1_name">徒然喜欢你</div>
              </a>
            </li>
          </ul>
        </div>
      </div>
      <div class="spaceblock1"></div>
      <div class="baseblock">
        <div class="blocktitle">留言板：</div>
        <div class="line"></div>

        <div class="blockcontent">
          <form id="comment_form" action="javascript:void(0)" method="GET">
            <div>
              <input id="comment_id" name="cid" type="hidden" value="20190209"/>
              <textarea id="comment_content" autocapitalize="off" autocomplete="off" autocorrect="off" csrf_token="kqYFPKw9DOko88jkfLKSATbDkz6ipQBD9pYXLohUtVGXpMhQPdkP7lyYGDwiNifm" name="comment_content" placeholder="说点什么吧" spellcheck="false" tid="20180132" wrap="SOFT"></textarea>
              <label class="comment_imform_tag">昵称：</label>
              <input id="comment_user" autocapitalize="off" autocomplete="off" autocorrect="off" class="" name="comment_user" placeholder="名字" readonly="true" spellcheck="false" value="游客" wrap="SOFT"/>
              <input class="nbutton" name="" type="submit" />
            </div>
          </form>
          <!-- 评论列表 -->
          <ul id="comment_list" style="margin-top: 16px">
            <li v-for="(item, index) in array" :key="index" class="comment">
              <hr class="hrspace2" />
              <div class="comment_cell_user">游客{{ index }}-120023001980153</div>
              <div class="comment_cell_content">
                <div>测试评论信息{{ index }}</div>
                <div class="comment_cell_time asciifont">2020-09-12 17:56:57</div>
              </div>
            </li>
            <hr class="hrspace2" />
            <div id="current_comment_page" page=""></div>
          </ul>
          <!-- 评论翻页 -->
          <div class="spaceblock1"></div>
          <ul class="comment_page">
            <li>
              <a class="pbutton pbutton_current asciifont" href="javascript:void(0)">首页</a>
            </li>
          </ul>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped src="../assets/css/play.css"></style>
