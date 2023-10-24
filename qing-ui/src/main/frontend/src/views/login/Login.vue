<script lang="ts" setup>
import { reactive } from "vue";
import { layer } from "@layui/layui-vue";
import { formLogin } from "@/api/auth";
import { userInfoStore } from "@/stores/session";

// “ref”是用来存储值的响应式数据源。
// 理论上我们在展示该字符串的时候不需要将其包装在 ref() 中，
// 但是在下一个示例中更改这个值的时候，我们就需要它了。
const loginForm = reactive({
  username: "",
  password: "",
});

function handleBtn() {
  layer.msg("进行登录操作");
  formLogin(loginForm.username, loginForm.password)
    .then(function (response) {
      console.log("发起请求成功了", response);
      const userInfoSto = userInfoStore();
      userInfoSto.accessToken = response.data.data.token;
      userInfoSto.username = "user那么少";
    })
    .catch(function (error) {
      console.log(error);
    });
}
</script>

<template>
  <lay-form
    :model="loginForm"
    :use-CN="true"
    required
    requiredIcons="layui-icon-heart-fill"
    style="text-align: center"
    @submit="handleBtn"
  >
    <lay-form-item label="账户" prop="username">
      <lay-input v-model="loginForm.username"></lay-input>
    </lay-form-item>
    <lay-form-item label="密码" prop="password">
      <lay-input v-model="loginForm.password"></lay-input>
    </lay-form-item>
    <lay-form-item style="text-align: center">
      <lay-button @click="handleBtn">提交</lay-button>
    </lay-form-item>
  </lay-form>
</template>

<style scoped src="../../assets/css/login.css"></style>
