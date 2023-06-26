<script lang="ts" setup>
import { onMounted } from "vue";
import { userInfoStore } from "@/stores/session";
import { exchangeToken } from "@/api/auth";
import { useRouter } from "vue-router";

// “ref”是用来存储值的响应式数据源。
// 理论上我们在展示该字符串的时候不需要将其包装在 ref() 中，
// 但是在下一个示例中更改这个值的时候，我们就需要它了。

onMounted(() => {
  console.log("登录中");
  const router = useRouter();

  const code = router.currentRoute.value.query.code;
  if (code === null) {
    alert("参数错误");
    return;
  }

  const state = router.currentRoute.value.query.state;
  if (state === null) {
    alert("参数错误");
    return;
  }

  console.log("query", code);
  console.log("state", state);

  exchangeToken(code.toString(), state.toString())
    .then(function (response) {
      console.debug("response", response.data.access_token);

      const userInfoSto = userInfoStore();

      userInfoSto.$reset();

      userInfoSto.login();
      userInfoSto.$patch((state) => {
        state.accessToken = response.data.access_token;
        state.expireAt = response.data.expires_in;
        state.idToken = response.data.id_token;
      });
      console.debug("本地存储信息", userInfoSto);
      router.push("/profile");
    })
    .catch((error) => {
      console.log("错误", error);
      alert("登录失败");
    });
});
</script>

<template>
  <div id="container">
    <h1>登录中。。。</h1>
  </div>
</template>

<style scoped src="../../assets/css/login.css"></style>
