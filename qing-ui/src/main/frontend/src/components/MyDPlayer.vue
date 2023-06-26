<template>
  <div>
    <div id="player" :style="dPlayerData.style"></div>
  </div>
</template>

<script lang="ts" setup>
import DPlayer from "dplayer";
import { onMounted, ref } from "vue";

const props = defineProps({
  player: DPlayer,
  likes: Number,
  url: String,
});

let dp = ref<DPlayer>();
const dPlayerData = ref({
  style: "width:100%;height:auto;position: absolute;bottom: 0;right: 0;",
});
const info = ref({ video: "视频链接", pic: "封面图链接" });

onMounted(() => {
  dp.value = new DPlayer({
    container: document.getElementById("player"),
    theme: "red", // 风格颜色，例如播放条，音量条的颜色
    loop: true, // 是否自动循环
    lang: "zh-cn", // 语言，'en', 'zh-cn', 'zh-tw'
    hotkey: true, // 是否支持热键，调节音量，播放，暂停等
    preload: "auto", // 自动预加载
    volume: 0.7, // 声音
    logo: "/favicon.ico",
    mutex: true,
    video: {
      url: `${props.url}`, // 播放视频的路径
      defaultQuality: 0, // 默认是HD
      pic: "", // 视频封面图片
    },
    contextmenu: [
      {
        text: "视频统计信息",
        link: "https://github.com/DIYgod/DPlayer",
      },
      {
        text: "custom2",
        click: () => {
          alert("点击了按钮");
        },
      },
    ],
    highlight: [
      {
        time: 20,
        text: "这是第 20 秒",
      },
      {
        time: 40,
        text: "这是 40 秒",
      },
    ],
  });
});
</script>
<style scoped></style>
