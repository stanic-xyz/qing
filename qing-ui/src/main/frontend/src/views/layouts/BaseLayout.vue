<script lang="ts" setup>
import QingHeader from "@/views/common/QingHeader.vue";
import QingFooter from "@/views/common/QingFooter.vue";
</script>

<template>
  <div>
    <QingHeader></QingHeader>
    <div id="main-content" class="x-container" style="min-height: 85vh">
      <router-view></router-view>
    </div>
    <QingFooter></QingFooter>
    <lay-backtop target="#main-content"></lay-backtop>
  </div>
</template>

<style>
@import "../../assets/css/common/base-css.css";
</style>
