·
<script lang="ts" setup>
import {onMounted, reactive} from "vue";

const state = reactive({
  count: 0,
  mediaList: [
    {
      media_id: 123,
      username: "陈云龙",
      badge: "这里是我发布的信息，你看吧",
      cover: "test",
    },
  ],
});

defineProps({
  mediaList: Array,
});

onMounted(() => {
  console.debug("fabule xnxi");
});
</script>

<template>
  <div>
    <iframe allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" allowfullscreen height="720" src="https://www.youtube.com/embed/CYIntGsjNMs" width="1280"></iframe>
  </div>
</template>

<style scoped>
div {
  background-color: red;
}
</style>
