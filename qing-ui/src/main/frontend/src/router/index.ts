import type {
  NavigationGuardNext,
  RouteLocationNormalized,
  Router,
} from "vue-router";
import { createRouter, createWebHistory } from "vue-router";
import routes from "./modules/base-routes";
import { userStore } from "@/stores/user";

/** 路由白名单 */
const whiteList = ["/login"];

/**
 * 创建路由实例
 */
const router: Router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
});

/**
 * Router 前置拦截
 *
 * 1.验证 token 存在, 并且有效, 否则 -> login.vue
 * 2.验证 permission 存在, 否则 -> 403.vue
 * 3.验证 router 是否存在, 否则 -> 404.vue
 *
 * @param to 目标
 * @param from 来至
 */
router.beforeEach(
  (
    to: RouteLocationNormalized,
    from: RouteLocationNormalized,
    next: NavigationGuardNext
  ): void => {
    var store = userStore();

    if (to.meta.requireAuth) {
      next();
    } else if (to.matched.length == 0) {
      next({ path: "/error/404" });
    } else {
      next();
    }
  }
);

router.onError(
  (error: any, to: RouteLocationNormalized, from: RouteLocationNormalized) => {
    console.log("导航守卫发生了错误");
  }
);

router.afterEach(() => {
  console.log("路由加载完毕");
});

export default router;