<script lang="ts" setup>
import { ref } from "vue";
import ExceptionPage from "@/views/exception/ExceptionPage.vue";

const count = ref(0);
</script>

<template>
  <exception-page type="404"></exception-page>
</template>

<style scoped></style>
