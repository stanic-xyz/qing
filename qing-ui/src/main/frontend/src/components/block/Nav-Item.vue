<template>
  <router-link
    :class="{ nav_button_current: isActive }"
    :to="navData.route"
    class="nav_button"
  >
    <span>{{ title }}</span>
  </router-link>
</template>
<script lang="ts" setup>
defineProps<{
  title?: string;
  isActive: boolean;
  navData: {
    route: "/test";
  };
}>();
</script>
