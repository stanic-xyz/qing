import Vue from 'vue'
import Router from 'vue-router'
import HomePage from '@/views/HomePage'

import LoginView from '@/views/login/login'
import db from '@/utils/localstorage'
import request from '@/utils/request'

// 全局Router异常处理
const originalPush = Router.prototype.push
Router.prototype.push = function push (location) {
  return originalPush.call(this, location).catch(err => { if (typeof err !== 'undefined') console.log(err) })
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
    component: HomePage
  }
]
let router = new Router({
  routes: constRouter
})
console.log(request)
const whiteList = ['/login']
console.log(whiteList)
// let asyncRouter

router.beforeEach((to, from, next) => {
  console.log(to, from, next)

  if (whiteList.indexOf(to.path) !== -1) {
    console.log('跳转到登陆页面！')
    next()
  }
  let token = db.get('USER_TOKEN')
  let user = db.get('USER')
  let userRouter = db.get('USER_ROUTER')
  console.log(token, user, userRouter)

  if (token.length && user) {
    console.log('登陆信息有效！')
    // 需要执行一些异步操作
    // if (!asyncRouter) {
    //   console.log('这里是啥东西呢！')
    //   if (!userRouter) {
    //     console.log('请求路由信息！')

    // save('USER_ROUTER', asyncRouter)

    // go(to, next)
    // }
    // }
  }
  next()
})

// // 导航守卫，渲染动态路由
// router.beforeEach((to, from, next) => {
//   if (whiteList.indexOf(to.path) !== -1) {
//     next()
//   }
//   let token = db.get('USER_TOKEN')
//   let user = db.get('USER')
//   let userRouter = get('USER_ROUTER')
//   if (token.length && user) {
//     if (!asyncRouter) {
//       if (!userRouter) {
//         request.get(`menu/${user.username}`).then((res) => {
//           asyncRouter = res.data
//           save('USER_ROUTER', asyncRouter)
//           go(to, next)
//         }).catch(err => { console.error(err) })
//       } else {
//         asyncRouter = userRouter
//         go(to, next)
//       }
//     } else {
//       next()
//     }
//   } else {
//     next('/login')
//   }
// })
// function go (to, next) {
//   asyncRouter = filterAsyncRouter(asyncRouter)
//   router.addRoutes(asyncRouter)
//   next({ ...to, replace: true })
// }

// function save (name, data) {
//   localStorage.setItem(name, JSON.stringify(data))
// }

// function get (name) {
//   return JSON.parse(localStorage.getItem(name))
// }

// function filterAsyncRouter (routes) {
//   return routes.filter((route) => {
//     let component = route.component
//     if (component) {
//       // switch (route.component) {
//       //   case 'MenuView':
//       //     route.component = MenuView
//       //     break
//       //   case 'PageView':
//       //     route.component = PageView
//       //     break
//       //   case 'EmptyPageView':
//       //     route.component = EmptyPageView
//       //     break
//       //   case 'HomePageView':
//       //     route.component = HomePageView
//       //     break
//       //   default:

//     }
//     route.component = view(component)
//     if (route.children && route.children.length) {
//       route.children = filterAsyncRouter(route.children)
//     }
//     return true
//   })
// }

// function view (path) {
//   return function (resolve) {
//     import(`@/views/${path}.vue`).then(mod => {
//       resolve(mod)
//     })
//   }
// }

export default router
