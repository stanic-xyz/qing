<script lang="ts" setup>
import {onMounted} from "vue";
import {userInfoStore} from "@/stores/session";
import {useRouter} from "vue-router";

// “ref”是用来存储值的响应式数据源。
// 理论上我们在展示该字符串的时候不需要将其包装在 ref() 中，
// 但是在下一个示例中更改这个值的时候，我们就需要它了。

onMounted(() => {
  console.log("登录中");
  const router = useRouter();

  const accessToken = router.currentRoute.value.query.accessToken;
  if (accessToken === null) {
    console.log("登录失败，token为空");
    return;
  }

  const state = router.currentRoute.value.query.tempUser;
  if (state === null) {
    return;
  }
  console.log("accessToken", accessToken);
  console.log("state", state);
  const userInfoSto = userInfoStore();
  userInfoSto.login(accessToken);
  console.log("登录成功:当前用户类型是否是临时用户：" + state.toString());
  router.push("/profile");
});
</script>

<template>
  <div id="container">
    <h1>登录中。。。</h1>
  </div>
</template>

<style scoped src="../../assets/css/login.css"></style>
