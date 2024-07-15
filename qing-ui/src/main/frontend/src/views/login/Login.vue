<template>
  <LayContainer class="h-100">
    <div class="my-login-page">
      <div class="row justify-content-md-center h-100">
        <div class="card-wrapper">
          <div class="brand">
            <img src="../../assets/img/logo.jpg" alt="logo"/>
          </div>
          <div class="card fat">
            <div class="card-body">
              <h4 class="card-title">登录</h4>
              <form method="POST" class="my-login-validation">
                <div class="form-group">
                  <label for="email">手机号/邮箱</label>
                  <input id="email" type="email" class="form-control" name="email" value="" required autofocus v-model="loginFormData.username"/>
                  <div class="invalid-feedback">Email is invalid</div>
                </div>
                <div class="form-group">
                  <label for="password">密码</label>
                  <input id="password" type="password" class="form-control" name="password" v-model="loginFormData.password" required data-eye/>
                  <div class="invalid-feedback">请输入密码！</div>
                </div>
                <div class="form-group">
                  <div class="custom-checkbox custom-control">
                    <input type="checkbox" name="remember" id="remember" class="custom-control-input" v-model="loginFormData.rememberMe"/>
                    <label for="remember" class="custom-control-label">记住账号</label>
                  </div>
                </div>
                <div class="form-group m-0">
                  <button type="submit" class="btn btn-primary btn-block" @click.prevent="handleBtn">Login</button>
                </div>
                <lay-space spaceAlign="center" :fill="false">
                  <div class="mt-4 text-center">没有账号？ <a href="register.html">创建</a>&nbsp;</div>
                  <div class="mt-4 text-center"><a href="forgot.html"> 忘记密码？</a></div>
                </lay-space>
              </form>
            </div>
          </div>
          <div class="footer">Copyright &copy; 2017 &mdash; Your Company</div>
        </div>
      </div>
    </div>
  </LayContainer>
</template>

<script lang="ts" setup>
import {reactive} from "vue";
import {LayContainer, layer} from "@layui/layui-vue";
import {userInfoStore} from "@/stores/session";
import {useRouter} from "vue-router";
import {formLogin} from "@/apis/auth";

// “ref”是用来存储值的响应式数据源。
// 理论上我们在展示该字符串的时候不需要将其包装在 ref() 中，
// 但是在下一个示例中更改这个值的时候，我们就需要它了。
const loginFormData = reactive({
  username: "",
  password: "",
  rememberMe: false,
});

const router = useRouter();
defineEmits(["loginSuccess"]);

const handleBtn = () => {
  // 模拟登录
  console.debug("正在登录");
  formLogin(loginFormData.username, loginFormData.password)
          .then(function (response) {
            console.log("发起请求成功了", response.data.data);
            const userInfoSto = userInfoStore();
            let token = response.data.data;
            userInfoSto.username = "user那么少";
            userInfoSto.login(token);
            router.push("/profile");
          })
          .catch(function (error) {
            console.log(error);
            layer.msg("登录失败");
          });
};
</script>
<style lang="scss" scoped>
@import url(../../assets/css/common/bootstrap.min.css);
@import url(../../assets/css/login/my-login.css);
</style>
