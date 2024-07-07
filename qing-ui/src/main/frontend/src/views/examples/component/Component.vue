<script lang="ts" setup>
import {onMounted, ref} from "vue";
import BlogPost from "@/views/examples/component/BlogPost.vue";

const posts = ref<any[]>([]);

const postFontSize = ref(1);

onMounted(() => {
  posts.value.push({
    id: "123123",
    title: "123123",
    content: "这是内容",
  });
});

function largeEvent() {
  console.log("调整大小时间");
}
</script>

<template>
  <BlogPost v-for="post in posts" :key="post.id" :title="post.title" @enlarge-text="largeEvent">
    {{ post.content }}
  </BlogPost>
</template>

<style scoped></style>
