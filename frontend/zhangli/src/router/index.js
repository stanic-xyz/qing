import Vue from 'vue'
import Router from 'vue-router'
import HelloWorld from '@/components/HelloWorld'
import LoginView from '@/views/login/login'
import db from '@/utils/localstorage'

// 全局Router异常处理
const originalPush = Router.prototype.push
Router.prototype.push = function push (location) {
  return originalPush.call(this, location).catch(err => { if (typeof err !== 'undefined')console.log(err) })
}
console.log(db)
Vue.use(Router)
let constRouter = [
  {
    path: '/login',
    name: '登录页',
    component: LoginView
  },
  {
    path: '/',
    name: '首页',
    component: HelloWorld
  }
]
let router = new Router({
  routes: constRouter
})

export default router
