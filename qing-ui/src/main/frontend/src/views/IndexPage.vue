<template>
  <div id="container"></div>
</template>

<script lang="ts" setup>
import { onMounted } from "vue";

onMounted(() => {
  console.log("onMounted");
});
</script>

<style scoped src="../assets/css/index.css">
#container {
  color: red;
}
</style>
