<template>
  <footer id="footer">
    <div id="footer_container">
      <div class="pannel">
        <div class="pannel_head">
          <h3 class="title">快速链接：</h3>
        </div>
        <ul class="link_text">
          <li>
            <a href="https://www.chenyunlong.cn" target="_blank" title="博客地址">博客地址</a>
          </li>
          <li>
            <a href="https://www.chenyunlong.cn/admin" target="_blank" title="博客后台地址">博客后台地址</a>
          </li>
          <li>
            <a href="https://status.chenyunlong.cn" target="_blank" title="状态查看">服务器状态</a>
          </li>
        </ul>

        <span class="email">
          如果你有任何关于此网站的问题，请发邮件至
          <a href="mailto:bangumi@chenyunlong.cn">bangumi@chenyunlong.cn</a>
        </span>
      </div>
    </div>
  </footer>
</template>
<script lang="ts" setup>
import {onMounted} from "vue";

onMounted(() => {
  console.log("Footer加载完毕！");
});
</script>
<style lang="scss" scoped>
#footer_container {
  max-width: 1140px;

  .link_text {
    padding: 10px 0;
    color: #ddd;

    li {
      margin-right: 10px;
      color: white;

      &:hover {
        text-decoration: underline;
      }

      a {
        color: #ddd;
      }
    }
  }

  .email {
    a {
      color: #ddd;

      &:hover {
        text-decoration: underline;
      }
    }
  }

  .pannel {
    .pannel_head {
      padding-bottom: 0.5rem;
      border-bottom: 1px solid rgba(255, 255, 255, 0.125);
    }
  }
}
</style>
