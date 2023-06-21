<script lang="ts" setup>
import { onMounted, reactive } from "vue";

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
  console.log("fabule xnxi");
});

function increment() {
  state.count++;
  state.mediaList = [
    {
      media_id: 123,
      username: "陈云龙",
      badge: "发布了信心了",
      cover: "test",
    },
  ];
}

function handleEdit(test: Number, tes2t: String) {
  console.log("handle function", test, tes2t);
}
</script>

<template>
  <div>
    <iframe
      allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture"
      allowfullscreen
      height="720"
      src="https://www.youtube.com/embed/CYIntGsjNMs"
      width="1280"
    ></iframe>
  </div>
</template>

<style scoped>
div {
  background-color: red;
}
</style>
