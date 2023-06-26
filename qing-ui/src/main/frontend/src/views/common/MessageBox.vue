<template>
  <div
    aria-labelledby="msg-box-txt-1672250102156"
    class="popup msg-box fade-in"
    role="dialog"
    style="top: calc(50% - 62px)"
  >
    <div id="msg-box-txt-1672250102156" class="msg-box-txt">密码错误。</div>
    <div class="btn-bar">
      <button class="cancel-btn default-btn" type="button" @click="close">
        确定
      </button>
    </div>
  </div>
</template>

<script lang="ts" setup>
function close() {
  alert("隐藏窗口");
}
</script>

<style scoped></style>
