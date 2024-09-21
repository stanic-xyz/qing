import {createRouter, createWebHashHistory, type NavigationGuardNext, type RouteLocationNormalized, type Router} from "vue-router";

import {userStore} from "@/stores/user";
import routerList from "@/router/modules/base-routes";

/** 自动导入全部静态路由，无需再手动引入！匹配 src/router/modules 目录（任何嵌套级别）中具有 .ts 扩展名的所有文件，除了 remaining.ts 文件
 * 如何匹配所有文件请看：https://github.com/mrmlnc/fast-glob#basic-syntax
 * 如何排除文件请看：https://cn.vitejs.dev/guide/features.html#negative-patterns
 */
const modules: Record<string, any> = import.meta.glob(["./modules/**/*.ts", "!./modules/**/remaining.ts"], {
    eager: true,
});

/** 原始静态路由（未做任何处理） */
const routes: any[] = routerList;

Object.keys(modules).forEach((key) => {
    routes.push(modules[key].default);
});

/** 路由白名单 */
const whiteList = ["/login"];

/**
 * 创建路由实例
 */
const router: Router = createRouter({
    history: createWebHashHistory(),
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
router.beforeEach((to: RouteLocationNormalized, from: RouteLocationNormalized, next: NavigationGuardNext): void => {
    console.log(to);
    // 在白名单内的路由，直接放行
    for (let i = 0; i < whiteList.length; i++) {
        if (to.path == whiteList[i]) {
            next();
            return
        }
    }
    const store = userStore();
    if (to.meta.requireAuth) {
        next();
    } else if (to.matched.length == 0) {
        // next({path: "/error/404"});
        next();
    } else {
        next();
    }
});

router.onError((error: any, to: RouteLocationNormalized, from: RouteLocationNormalized) => {
    console.log("导航守卫发生了错误");
});

router.afterEach(() => {
    console.log("路由加载完毕");
});

export default router;
