<script lang="ts" setup>
import { ref } from "vue";

const count = ref(0);

console.log("ExceptionPage 加载成功");
</script>

<template>
  <header
    class="panel-cover"
    style="background-image: url('../../../public/logo.jpg')"
  >
    <div class="panel-main">
      <div class="panel-main__inner panel-inverted">
        <div class="panel-main__content">
          <a href="/login">
            <img
              alt="AGE FANS LOGO"
              class="panel-cover__logo logo"
              src="../../../public/logo.jpg"
              title="AGE动漫"
              width="150"
          /></a>
          <hr class="panel-cover__divider" />
          <p class="panel-cover__description">
            对不起，您要找的页面被俺给弄丢了(＞人＜){{ count }}
          </p>
          <hr class="panel-cover__divider panel-cover__divider--secondary" />
          <p class="footer">我的动漫 © 2020 - 2022 bangumi.chenyunlong.cn</p>
        </div>
      </div>
      <div class="panel-cover--overlay cover-slate"></div>
    </div>
  </header>
</template>

<style scoped>
.panel-cover {
  position: fixed;
  z-index: 900;
  display: block;
  width: 100%;
  height: 100%;
  max-width: none;
  background: url("../../../public/logo.jpg") top left no-repeat #666666;
  background-size: cover;
}
</style>
