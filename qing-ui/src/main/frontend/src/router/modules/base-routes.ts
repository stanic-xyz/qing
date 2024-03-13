import Activity from "@/views/Activity.vue";
import AuthingLogin from "@/views/login/AuthingLogin.vue";
import ExceptionPage from "@/views/exception/ExceptionPage.vue";
import Login from "@/views/login/Login.vue";
import BaseLayout from "@/views/layouts/BaseLayout.vue";

export default [
  {
    path: "/",
    redirect: "/home",
  },
  {
    path: "/login",
    component: Login,
    meta: { title: "登录页面" },
  },
  {
    path: "/home",
    redirect: "/home",
    component: BaseLayout,
    meta: { title: "工作空间" },
    children: [
      {
        path: "/home",
        name: "Home",
        component: () => import("../../views/home/Home.vue"),
        meta: {
          title: "工作台",
          requireAuth: true,
          affix: true,
          closable: false,
        },
      },
      {
        path: "/recommend",
        name: "recommend",
        component: () => import("../../views/recommend/Recommend.vue"),
        meta: {
          title: "工作台",
          requireAuth: true,
          affix: true,
          closable: false,
        },
      },
      {
        path: "/update",
        name: "update",
        component: () => import("../../views/UpdatePage.vue"),
        meta: {
          title: "工作台",
          requireAuth: true,
          affix: true,
          closable: false,
        },
      },
      {
        path: "/catalog",
        name: "catalog",
        component: () => import("@/views/catalog/Catalog.vue"),
        meta: {
          title: "工作台",
          requireAuth: true,
          affix: true,
          closable: false,
        },
      },
      {
        path: "/rank",
        name: "rank",
        component: () => import("../../views/RankPage.vue"),
        meta: {
          title: "工作台",
          requireAuth: true,
          affix: true,
          closable: false,
        },
      },
      {
        path: "/anime/:animeId",
        name: "anime",
        component: () => import("../../views/Detail.vue"),
        meta: {
          title: "工作台",
          requireAuth: true,
          affix: true,
          closable: false,
        },
      },
      {
        path: "/examples/component",
        name: "example-component",
        component: () => import("../../views/examples/component/Component.vue"),
        meta: {
          title: "组件",
          requireAuth: false,
          affix: true,
          closable: false,
        },
      },
      {
        path: "/play/:animeId",
        name: "play",
        component: () => import("../../views/PlayPage.vue"),
      },
      {
        path: "/profile",
        name: "profile",
        component: () => import("../../views/about/About.vue"),
      },
      {
        path: "/search",
        name: "search",
        component: () => import("../../views/SearchPage.vue"),
      },
      {
        path: "/login",
        name: "login",
        meta: {
          requiresAuth: false,
        },
        component: () => import("../../views/login/Login.vue"),
      },
    ],
  },

  { path: "/users/:id", component: Activity },
  { path: "/login/authing", component: AuthingLogin },

  {
    path: "/error/401",
    component: () => import("../../views/error/401.vue"),
    meta: { title: "401" },
  },
  {
    path: "/error/403",
    component: () => import("../../views/error/403.vue"),
    meta: { title: "403" },
  },
  {
    path: "/error/404",
    component: () => import("../../views/error/404.vue"),
    meta: { title: "404" },
  },
  {
    path: "/error/500",
    component: () => import("../../views/error/500.vue"),
    meta: { title: "500" },
  },
  // 将匹配所有内容并将其放在 `$route.params.pathMatch` 下
  { path: "/:pathMatch(.*)*", name: "NotFound", component: ExceptionPage },
];
