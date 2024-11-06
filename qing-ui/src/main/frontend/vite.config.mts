import { fileURLToPath, URL } from "node:url";

import { defineConfig } from "vite";
import vue from "@vitejs/plugin-vue";
import vueJsx from "@vitejs/plugin-vue-jsx";

// https://vitejs.dev/config/
export default defineConfig({
    plugins: [
        vue(),
        vueJsx(),
        // AutoImport({
        //     resolvers: [LayuiVueResolver()],
        // }),
        // Components({
        //     resolvers: [LayuiVueResolver()],
        // }),
    ],
    resolve: {
        alias: {
            "@": fileURLToPath(new URL("./src", import.meta.url)),
        },
    },
    build: {
        rollupOptions: {},
    },
    css: {
        preprocessorOptions: {
            scss: {
                api: "modern-compiler", // or "modern"
            },
        },
    },
    server: {
        proxy: {
            [`^/api`]: {
                target: "http://192.168.3.3:8080",
                changeOrigin: false,
                bypass(req, res, options) {
                    const proxyURL = options.target + req.url;
                    res.setHeader("x-req-proxyURL", proxyURL); // 将真实请求地址设置到响应头中
                },
            },
        },
    },
});
