<script lang="ts" setup>
import QingHeader from "@/views/common/QingHeader.vue";
import QingFooter from "@/views/common/QingFooter.vue";
</script>

<template>
  <div>
    <QingHeader></QingHeader>
    <div class="base-container">
      <div class="container">
        <router-view></router-view>
      </div>
    </div>
    <QingFooter></QingFooter>
  </div>
</template>

<style scoped>
@import url(../../assets/css/common/base.css);

.base-container {
  width: 1140px;
  max-width: 1140px;
  margin: 1rem auto 0;
  padding: 0 1rem 0;

  .container {
    border: 4px solid #303030;
    background-color: #202020;
    min-height: calc(100vh - 160px - 56px - 128px - 8px);
    padding: 1rem 1rem;
  }
}
</style>
