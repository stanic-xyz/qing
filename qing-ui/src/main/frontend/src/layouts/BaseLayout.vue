<script setup lang="ts">
import MyFooter from "@/views/common/MyFooter.vue";
import MyHeader from "@/views/common/MyHeader.vue";
</script>

<template>
  <lay-layout class="root">
    <lay-header style="height: auto">
      <my-header></my-header>
    </lay-header>
    <lay-body>
      <router-view></router-view>
    </lay-body>
    <lay-footer>
      <my-footer></my-footer>
    </lay-footer>
  </lay-layout>
</template>

<style scoped></style>
