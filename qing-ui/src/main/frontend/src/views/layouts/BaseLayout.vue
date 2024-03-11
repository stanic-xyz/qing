<script lang="ts" setup>
import MyFooter from "@/views/common/QingFooter.vue";
import MyHeader from "@/views/common/QingHeader.vue";
import QingHeader from "@/views/common/QingHeader.vue";
import QingFooter from "@/views/common/QingFooter.vue";
</script>

<template>
  <div class="base-wrapper">
    <QingHeader></QingHeader>
    <router-view class="base-container"></router-view>
    <QingFooter></QingFooter>
  </div>
</template>

<style scoped>
@import url(../../assets/css/common/base.css);

.base-container {
  width: 100vw;
  margin: 1rem auto 0;
}
</style>
