<template>
  <LayContainer class="layui-container">
    <div class="login-panel">
      <div class="content-panel">
        <div class="login-logo">
          <img alt="登录Logo" src="../../assets/logo.png"/>
        </div>
        <div class="split-line"></div>
        <div class="login-form-wrapper">
          <LayForm :model="loginFormData" :pane="pine" :use-CN="true" class="login-form" required requiredIcons="layui-icon-heart-fill" size="md" style="color: whitesmoke" @submit="handleBtn">
            <lay-form-item label="账户" prop="username">
              <lay-input v-model="loginFormData.username"></lay-input>
            </lay-form-item>
            <lay-form-item label="密码" prop="password">
              <LayInput v-model="loginFormData.password" type="password"></LayInput>
            </lay-form-item>
            <LayFormItem>
              <lay-button style="display: inline-block" type="primary" @click="handleBtn">提交</lay-button>
            </LayFormItem>
          </LayForm>
        </div>
      </div>
    </div>
  </LayContainer>
</template>

<script lang="ts" setup>
import {reactive, ref} from "vue";
import {LayContainer, layer, LayForm, LayFormItem} from "@layui/layui-vue";
import {userInfoStore} from "@/stores/session";
import {useRouter} from "vue-router";

// “ref”是用来存储值的响应式数据源。
// 理论上我们在展示该字符串的时候不需要将其包装在 ref() 中，
// 但是在下一个示例中更改这个值的时候，我们就需要它了。
const loginFormData = reactive({
  username: "",
  password: "",
});

const pine = ref(true);
const router = useRouter();
defineEmits(["loginSuccess"]);

const handleBtn = () => {
  // 模拟登录
  const userInfoSto = userInfoStore();
  let token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiLnlKjmiLc4MTg1ZTgyNDU1IiwiY3JlYXRlZCI6MTcxMDAwMTY5MzM0MywiZXhwIjoyNTc0MDAyOTMxfQ.84k9O6Ares7s8BqhrR12-WDe7z9T9cEPe8uC4s38qe44ExYhNrBY-uHkuWTq0TImE_AF5KjpCCgYUPTQJegOqA";
  userInfoSto.username = "user那么少";
  userInfoSto.login(token);
  layer.msg("登录成功");
  console.log(router);
  router.push("/profile");
  // formLogin(loginFormData.username, loginFormData.password)
  //   .then(function (response) {
  //     console.log("发起请求成功了", response);
  //     const userInfoSto = userInfoStore();
  //     let token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiLnlKjmiLc4MTg1ZTgyNDU1IiwiY3JlYXRlZCI6MTcxMDAwMTY5MzM0MywiZXhwIjoyNTc0MDAyOTMxfQ.84k9O6Ares7s8BqhrR12-WDe7z9T9cEPe8uC4s38qe44ExYhNrBY-uHkuWTq0TImE_AF5KjpCCgYUPTQJegOqA";
  //     userInfoSto.username = "user那么少";
  //     userInfoSto.login(token);
  //   })
  //   .catch(function (error) {
  //     console.log(error);
  //     layer.msg("登录失败");
  //   });
};
</script>
<style lang="scss" scoped>
//@import url(../../assets/base.css);

.layui-container {
  padding: 0;
  background-color: #000000;

  .login-panel {
    margin: 0;
    padding: 0;
    display: flex;
    justify-items: center;
    justify-content: center;
    align-items: center;
    height: 100vh;
    width: 100vw;
    background-image: url("../../assets/img/loginbg.jpg");
  }

  .content-panel {
    border-radius: 10px;
    background-color: rgb(40, 40, 40);
    border: 1px solid red;
    margin: 0 auto 0;
    height: 50vh;
    width: 50vw;
    display: flex;
    flex-direction: row;
    justify-items: center;
    align-content: center;
    justify-content: space-around;

    .login-logo {
      width: 40%;
      display: flex;
      justify-items: center;
      justify-content: center;
      align-items: center;
      border-radius: 10px;

      img {
        width: 100px;
        height: 100px;
      }
    }

    .split-line {
      width: 2px;
      height: 100%;
      background: linear-gradient(135deg, #02835300, #be8f8f, #02835300);
    }

    .login-form {
      color: white;
      flex: 1;
      width: 400px;
      max-width: 100%;
      display: flex;
      flex-direction: column;
      justify-items: center;
      justify-content: center;

      input {
        color: white;
        background-color: blue;
        flex: 1;
        width: 400px;
        max-width: 100%;
      }
    }
  }
}
</style>
