import type { RouteLocationNormalized, Router } from "vue-router";
import { createRouter, createWebHistory } from "vue-router";
import Activity from "@/views/Activity.vue";
import LoginPage from "@/views/login/LoginPage.vue";
import ExceptionPage from "@/views/exception/ExceptionPage.vue";
import AuthingLogin from "@/views/login/AuthingLogin.vue";

/** 原始静态路由（未做任何处理） */
const routes = [];

/** 路由白名单 */
const whiteList = ["/login"];

/**
 * 创建路由实例
 */
const router: Router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/index",
      name: "index",
      component: () => import("../views/home/Home.vue"),
    },
    {
      path: "/",
      name: "home",
      component: () => import("../views/home/Home.vue"),
    },
    {
      path: "/recommend",
      name: "recommend",
      component: () => import("../views/Recommend.vue"),
    },
    {
      path: "/update",
      name: "update",
      component: () => import("../views/UpdatePage.vue"),
    },
    {
      path: "/catalog",
      name: "catalog",
      component: () => import("../views/catlog/Catalog.vue"),
    },
    {
      path: "/rank",
      name: "rank",
      component: () => import("../views/RankPage.vue"),
    },
    {
      path: "/anime/:animeId",
      name: "anime",
      component: () => import("../views/Detail.vue"),
    },
    {
      path: "/play/:animeId",
      name: "play",
      component: () => import("../views/PlayPage.vue"),
    },
    {
      path: "/profile",
      name: "profile",
      component: () => import("../views/about/About.vue"),
    },
    {
      path: "/login",
      name: "login",
      meta: {
        requiresAuth: false,
      },
      component: () => import("../views/login/LoginPage.vue"),
    },

    { path: "/users/:id", component: Activity },
    { path: "/login", component: LoginPage },
    { path: "/login/authing", component: AuthingLogin },

    {
      path: "/error/401",
      component: () => import("../views/error/401.vue"),
      meta: { title: "401" },
    },
    {
      path: "/error/403",
      component: () => import("../views/error/403.vue"),
      meta: { title: "403" },
    },
    {
      path: "/error/404",
      component: () => import("../views/error/404.vue"),
      meta: { title: "404" },
    },
    {
      path: "/error/500",
      component: () => import("../views/error/500.vue"),
      meta: { title: "500" },
    },
    // 将匹配所有内容并将其放在 `$route.params.pathMatch` 下
    { path: "/:pathMatch(.*)*", name: "NotFound", component: ExceptionPage },
  ],
});

router.beforeEach((to, from) => {
  // 我们想用这里的 store
  console.log("检查路由：", to.meta.name != "login");
  // const store = userInfoStore();
  // // 检查用户是否已登录
  // if (!store.isLoggedIn() && to.name !== "login") {
  //   // 将用户重定向到登录页面
  //   return { name: "login" };
  // }
  return true;
});

router.onError(
  (error: any, to: RouteLocationNormalized, from: RouteLocationNormalized) => {
    console.log("导航守卫发生了错误");
  }
);

router.afterEach(() => {
  console.log("路由加载完毕");
});

export default router;
