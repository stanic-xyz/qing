<template>
  <hr class="hrspace clear" style="width: 98%" />
  <div class="blocktitle">
    <router-link to="/update">最近更新</router-link>
  </div>
</template>

<script lang="ts" setup></script>

<style scoped>
.baseblock .blocktitle {
  padding: 6px 8px;
  color: #888899;
  font-size: 1.15em;
}

.baseblock .blocktitle * {
  color: #888899;
}

/*很坑的写法*/
.baseblock .blocktitle a {
  color: #e8e8e9;
  text-decoration-color: #bbbbbc;
}

.baseblock .blockcontent {
  padding: 10px 8px;
}

.baseblock .line {
  border-top: 1px dashed #404041;
  margin: 0 8px;
}
</style>
