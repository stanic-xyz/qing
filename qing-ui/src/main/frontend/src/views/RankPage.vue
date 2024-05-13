<template>
  <div>排行榜</div>
</template>

<script lang="ts" setup>
import {reactive} from "vue";

const data = reactive({
  activeIndex: "1",
  activeIndex2: "1",
  currentDate: new Date(),
  time: "2020年01月15日 21时50分19秒",
  loading: true,
});
</script>
<style scoped></style>
