<template>
  <div>
    <div
      style="
        color: white;
        padding: 8px;
        margin: 10px 9px -14px;
        font-size: 14px;
        border: 1px solid #cc0000;
      "
    >
      <span>
        本站地址：<a href="https://bangumi.chenyunlong.cn" target="_blank"
          >https://bangumi.chenyunlong.cn</a
        >欢迎大家分享给身边朋友！如果能够正常观看，那才是怪事了！</span
      >
    </div>
    <div class="spaceBlock1"></div>
  </div>
</template>
<script lang="ts">
export default {
  name: "MyTips",
  data() {
    return {
      time: new Date(),
      activeIndex: "1",
      activeIndex2: "1",
    };
  },
  methods: {
    create: function () {},
    handleSelect(key: string, keyPath: string) {
      console.log(key, keyPath);
    },
  },
};
</script>
<style scoped></style>
