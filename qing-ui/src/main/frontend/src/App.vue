<script lang="ts" setup>
import { RouterView } from "vue-router";
import MyHeader from "@/views/common/MyHeader.vue";
import MyFooter from "@/views/common/MyFooter.vue";
</script>

<template>
  <div class="root">
    <MyHeader />
    <RouterView />
    <MyFooter />
  </div>
</template>

<style scoped>
body {
  margin: 0;
}
</style>
