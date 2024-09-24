<template>
  <div class="qing-footer">
    <div class="footer_container x-container">
      <div>快速链接</div>
      <div class="link_text">
        <a href="https://www.chenyunlong.cn" target="_blank" title="博客地址">博客地址</a>
        <a href="https://www.chenyunlong.cn/admin" target="_blank" title="博客后台地址">博客后台地址</a>
        <a href="https://status.chenyunlong.cn" target="_blank" title="状态查看">服务器状态</a>
      </div>
      <span class="email">
        如果你有任何关于此网站的问题，请发邮件至
        <a href="mailto:bangumi@chenyunlong.cn">bangumi@chenyunlong.cn</a>
      </span>
      <div class="email">Copyright © 2024 动漫狂人</div>
    </div>
  </div>
</template>
<script lang="ts" setup>
import { onMounted } from "vue";

onMounted(() => {
  console.log("Footer加载完毕！");
});
</script>
<style lang="scss" scoped>
.qing-footer {
  width: 100%;
  background-color: #212529;
  padding: 16px;
  color: #e8e8e8;

  .footer_container {
    width: 1140px;
    max-width: 1140px;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    color: #e8e8e8;

    a {
      padding-right: 16px;
      color: #e8e8e8;

      &:hover {
        color: red;
      }
    }
  }
}
</style>
