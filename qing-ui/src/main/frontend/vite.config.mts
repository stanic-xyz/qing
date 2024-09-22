import {fileURLToPath, URL} from "node:url";

import {defineConfig, splitVendorChunkPlugin} from "vite";
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
        splitVendorChunkPlugin(),
    ],
    resolve: {
        alias: {
            "@": fileURLToPath(new URL("./src", import.meta.url)),
        },
    },
    build: {
        rollupOptions: {},
    },
    server: {
        proxy: {
            [`^/api`]: {
                target: "http://192.168.3.3:8080",
                changeOrigin: true,
            },
        },
    },
});
