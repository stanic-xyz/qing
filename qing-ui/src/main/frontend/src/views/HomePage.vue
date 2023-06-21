<template>
  <!-- <el-calendar v-model="currentDate"></el-calendar> -->
  <HelloWorld :title="data.activeIndex2"></HelloWorld>
</template>

<script lang="ts" setup>
import HelloWorld from "./anime/HelloWorld.vue";
import { onMounted, reactive } from "vue";

const data = reactive({
  activeIndex: "1",
  activeIndex2: "1",
  currentDate: new Date(),
  time: "2020年01月15日 21时50分19秒",
  loading: true,
  jvm: {
    memory: {
      max: 0,
      committed: 0,
      used: 0,
    },
    buffer: {
      memory: {
        used: 0,
      },
      count: 0,
    },
    threads: {
      daemon: 0,
      live: 0,
      peak: 0,
    },
    classes: {
      loaded: 0,
      unloaded: 0,
    },
    gc: {
      memory: {
        allocated: 0,
        promoted: 0,
      },
      maxDataSize: 0,
      liveDataSize: 0,
      pause: {
        totalTime: 0,
        count: 0,
      },
    },
  },
});

onMounted(() => {
  console.log("mounted");
});
</script>
<style scoped></style>
