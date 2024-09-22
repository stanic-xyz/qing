<script lang="ts" setup>
import QingHeader from "@/views/common/QingHeader.vue";
import QingFooter from "@/views/common/QingFooter.vue";
</script>

<template>
  <div>
    <QingHeader></QingHeader>
    <div class="x-container">
      <router-view></router-view>
    </div>
    <QingFooter></QingFooter>
  </div>
</template>

<style>
@import "../../assets/css/common/base-css.css";
</style>
