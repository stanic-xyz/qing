<script lang="ts" setup>
import LoginVue from "../login/LoginPage.vue";

const props = defineProps({
  todo: String,
});
</script>

<template>
  <li>{{ props.todo }}</li>
  <LoginVue></LoginVue>
</template>
