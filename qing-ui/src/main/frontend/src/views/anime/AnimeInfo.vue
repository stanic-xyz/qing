<script lang="ts" setup>
import {onMounted, type PropType, ref} from "vue";
import type {Anime} from "@/apis/anime/types";

onMounted(() => {
  console.log("动漫详情加载成功", props.anime.id);
});

const animeId = ref(1);
const props = defineProps({
  anime: {
    type: Object as PropType<Anime>,
    required: false,
    name: {
      type: String,
      default: "动漫名称",
    },
    default: {} as Anime,
  },
});
</script>

<template>
  <div class="card-container" style="width: 130px; margin: 15px; padding: 5px">
    <router-link :to="`13123-${anime.id}`">
      <div class="video_item">
        <div class="video_item--image position-relative"><img :alt="anime.name" :src="anime.coverUrl" class="video_thumbs lazyload d-block w-100" data-original="https://cdn.aqdstatic.com:966/age/20220255.jpg" referrerpolicy="no-referrer" style="display: block; width: 120px"/><span class="video_item--info rounded-1 text-truncate">第08集</span></div>
        <div class="video_item-title text-truncate text-center py-2" style="color: black">
          <div class="link-light text-decoration-none stretched-link">
            {{ anime.name }}
          </div>
        </div>
      </div>
    </router-link>
  </div>
</template>

<style scoped>
.card-container {
  background: whitesmoke;
  padding: 0;
  width: auto;
}
</style>
