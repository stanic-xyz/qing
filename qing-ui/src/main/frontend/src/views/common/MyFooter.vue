<template>
  <footer id="footer">
    <div id="footer_container">
      <div class="pannel clearfix">
        <div class="pannel_head clearfix">
          <h3 class="title">快速链接：</h3>
        </div>
        <ul class="link_text list_scroll clearfix">
          <li>
            <a
              class="text_muted"
              href="https://www.chenyunlong.cn"
              target="_blank"
              title="博客地址"
              >博客地址</a
            >
          </li>
          <li>
            <a
              class="text_muted"
              href="https://www.chenyunlong.cn/admin"
              target="_blank"
              title="博客后台地址"
              >博客后台地址</a
            >
          </li>
          <li>
            <a
              class="text_muted"
              href="https://status.chenyunlong.cn"
              target="_blank"
              title="状态查看"
              >服务器状态</a
            >
          </li>
        </ul>

        <span style="color: #ddd">
          如果你有任何关于此网站的问题，请发邮件至
          <a href="mailto:bangumi@chenyunlong.cn">bangumi@chenyunlong.cn</a>
        </span>
      </div>
    </div>
  </footer>
</template>
<script lang="ts" setup>
import { onMounted, ref } from "vue";

const data = ref({
  time: new Date(),
  activeIndex: "1",
  activeIndex2: "1",
});

function handleSelect(key: string, keyPath: string) {
  console.log(key, keyPath);
}

onMounted(() => {
  console.log(data);
});
</script>
<style scoped>
.link_text {
  padding: 10px 0;
}

.link_text li {
  margin-right: 10px;
}
</style>
