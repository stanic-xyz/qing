import AuthingLogin from "@/views/login/AuthingLogin.vue";
import ExceptionPage from "@/views/exception/ExceptionPage.vue";
import BaseAdminLayout from "@/views/layouts/BaseAdminLayout.vue";
import Login from "@/views/login/Login.vue";
import BaseLayout from "@/views/layouts/BaseLayout.vue";
import Activity from "@/views/Activity.vue";
import AnimeManage from "@/views/admin/AnimeManage.vue";
import Home from "@/views/home/Home.vue";
import Recommend from "@/views/recommend/Recommend.vue";
import UpdatePage from "@/views/UpdatePage.vue";
import Catalog from "@/views/catalog/Catalog.vue";
import RankPage from "@/views/RankPage.vue";
import Detail from "@/views/Detail.vue";
import Component from "@/views/examples/component/Component.vue";
import PlayPage from "@/views/PlayPage.vue";
import About from "@/views/about/About.vue";
import SearchPage from "@/views/SearchPage.vue";

const routerList = [
    {
        path: "/",
        redirect: "/home",
    },
    {
        path: "/dashboard",
        component: BaseAdminLayout,
        meta: {title: "管理控制台"},
        children: [
            {
                path: "/dashboard",
                name: "dashboard",
                component: () => AnimeManage,
                meta: {
                    title: "工作台",
                    requireAuth: true,
                    affix: true,
                    closable: false,
                },
            },
        ],
    },
    {
        path: "/login",
        component: Login,
        meta: {title: "登录页面"},
    },
    {
        path: "/manager",
        component: AnimeManage,
        meta: {title: "动漫管理界面"},
    },
    {
        path: "/home",
        redirect: "/home",
        component: BaseLayout,
        meta: {title: "工作空间"},
        children: [
            {
                path: "/home",
                name: "Home",
                component: Home,
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
                component: Recommend,
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
                component: UpdatePage,
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
                component: Catalog,
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
                component: RankPage,
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
                component: Detail,
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
                component: Component,
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
                component: PlayPage,
            },
            {
                path: "/profile",
                name: "profile",
                component: About,
            },
            {
                path: "/search",
                name: "search",
                component: SearchPage,
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
    {path: "/users/:id", component: Activity},
    {path: "/login/authing", component: AuthingLogin},
    {
        path: "/error/401",
        component: () => import("../../views/error/401.vue"),
        meta: {title: "401"},
    },
    {
        path: "/error/403",
        component: () => import("../../views/error/403.vue"),
        meta: {title: "403"},
    },
    {
        path: "/error/404",
        component: () => import("../../views/error/404.vue"),
        meta: {title: "404"},
    },
    {
        path: "/error/500",
        component: () => import("../../views/error/500.vue"),
        meta: {title: "500"},
    },
    // 将匹配所有内容并将其放在 `$route.params.pathMatch` 下
    {path: "/:pathMatch(.*)*", name: "NotFound", component: ExceptionPage},
];

// 假设你有一个pages文件夹，里面包含了多个页面组件
const pages = import.meta.glob("../../views/anime/*.vue");

console.log("读取到所有的页面信息", pages);

// Object.keys(pages).forEach((key) => {
//     console.log("modList", key);
//
//     const name = key
//             .replace(/^\.\.\/\.\.\/views\/anime\//, "")
//             .replace(/\.vue$/, "")
//             .toLowerCase();
//
//     console.log("name", name);
//
//     const mod = pages[key];
//
//     console.log("modList", mod);
//
//     // routerList.push({
//     //     path: `/anime/${name}`,
//     //     name: name,
//     //     component: mod,
//     // });
// });

export default routerList;
