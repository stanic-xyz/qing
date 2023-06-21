<template>
  <div class="notification">
    本站地址：
    <a :href="url">{{ url }}</a>
    欢迎大家分享给身边朋友！如果能够正常观看，那才是怪事了
  </div>
</template>

<script lang="ts" setup>
const props = defineProps({
  url: String,
});
</script>

<style scoped>
.notification {
  color: white;
  padding: 10px;
  margin: 0;
  font-size: 14px;
  border: 1px solid red;
}
</style>
