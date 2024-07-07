<script lang="ts" setup>
import {ref} from "vue";

const posts = ref([]);

const postFontSize = ref(1);

const props = defineProps(["title"]);
const emits = defineEmits(["enlarge-text"]);
</script>

<template>
  <div class="blog-post">
    <h4>{{ props.title }}</h4>
    <button @click="$emit('enlarge-text')">Enlarge text</button>
    <div>
      <slot/>
    </div>
  </div>
</template>

<style scoped></style>
