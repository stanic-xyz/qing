<script lang="ts" setup>
import { ref } from "vue";

const username = ref("username");
</script>

<template>
  <div class="about">
    {{ username }}
    <h1>This is an about page</h1>
  </div>
</template>

<style scoped></style>
