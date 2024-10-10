<template>
  <div id="container"></div>
</template>

<script lang="ts" setup>
import {onMounted} from "vue";

onMounted(() => {
  console.log("onMounted");
  console.log("this 充 home");
});
</script>

<style scoped src="../assets/css/index.css">
#container {
  color: red;
}
</style>
