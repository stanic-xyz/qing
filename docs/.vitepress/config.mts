import {defineConfig} from 'vitepress'

// https://vitepress.dev/reference/site-config
export default defineConfig({
    base: "/qing",
    title: "Qing",
    description: "项目文档",
    head: [
        ['link', {rel: 'icon', type: 'image/svg+xml', href: '/logo-mini.svg'}],
        ['link', {rel: 'icon', type: 'image/png', href: '/favicon.png'}],
    ],
    themeConfig: {
        search: {
            provider: 'local',
            options: {
                locales: {
                    zh: {
                        translations: {
                            button: {
                                buttonText: '搜索文档',
                                buttonAriaLabel: '搜索文档'
                            },
                            modal: {
                                noResultsText: '无法找到相关结果',
                                resetButtonTitle: '清除查询条件',
                                footer: {
                                    selectText: '选择',
                                    navigateText: '切换'
                                }
                            }
                        }
                    }
                },
                miniSearch: {
                    searchOptions: {
                        combineWith: 'AND',
                        fuzzy: 0.2,
                        prefix: true,
                        boost: {title: 4, text: 2, titles: 1}
                    },
                    options: {
                        fields: ['title', 'text', 'titles'],
                        storeFields: ['title', 'text', 'titles']
                    }
                }
            }
        },
        logo: {
            src: '/logo-mini.svg',
            alt: 'Qing',
            width: 24, height: 24
        },
        // https://vitepress.dev/reference/default-theme-config
        nav: [
            {text: '快速开始', link: '/快速开始'},
            {
                text: '文档指南',
                items: [
                    {text: '用户指南', link: '/用户指南'},
                    {text: '开发指南', link: '/category/开发者指南'},
                    {text: '架构设计', link: '/架构设计'},
                    {text: '部署指南', link: '/部署指南'}
                ]
            },
            {text: '代码生成器', link: '/category/代码生成器'},
            {text: 'API文档', link: '/api-examples'}
        ],
        sidebar: {
            '/': [
                {
                    text: '快速开始',
                    collapsed: false,
                    items: [
                        {text: '项目简介', link: '/introduction'},
                        {text: '快速开始', link: '/快速开始'},
                        {text: '用户指南', link: '/用户指南'}
                    ]
                },
                {
                    text: '开发指南',
                    collapsed: false,
                    items: [
                        {text: '开发者指南', link: '/category/开发者指南'},
                        {text: '代码生成器', link: '/category/代码生成器'},
                        {text: '参与贡献', link: '/category/参与贡献'}
                    ]
                },
                {
                    text: '架构与设计',
                    collapsed: true,
                    items: [
                        {text: '架构设计', link: '/架构设计'},
                        {text: '总体设计', link: '/design/动漫管理网站'},
                        {text: '数据库设计', link: '/db/readme'}
                    ]
                },
                {
                    text: '部署运维',
                    collapsed: true,
                    items: [
                        {text: '部署指南', link: '/部署指南'},
                        {text: 'DevOps', link: '/DEVOPS'}
                    ]
                },
                {
                    text: '文档与博客',
                    collapsed: true,
                    items: [
                        {text: '关于文档', link: '/category/关于文档'},
                        {
                            text: 'K3s部署',
                            link: '/博客/K3s环境使用Let\'s Encrypt证书的部署及自动配置https域名-阿里云域名解析管理'
                        },
                        {text: '安装Ubuntu', link: '/博客/在Ubuntu中安装PostgreSql并配置远程访问'},
                        {text: '安装Harbor', link: '/博客/安装Harbor'},
                        {text: 'Docker镜像', link: '/博客/搭建Docker镜像加速'}
                    ]
                }
            ]
        },
        socialLinks: [
            {icon: 'github', link: 'https://github.com/stanic-xyz/qing'}
        ],
        footer: {
            message: '在 Mulan PSL v2 协议下开源，请遵守开源协议.',
            copyright: 'Copyright © 2018-2024 青(Qing)项目团队'
        }
    },
    sitemap: {
        hostname: 'https://docs.qing.dev'
    },
    lastUpdated: true,
    ignoreDeadLinks: true,
    cleanUrls: true,
    metaChunk: true,
    mpa: false,
    appearance: 'dark',
    lang: 'zh-CN',
    transformHead: ({pageData}) => {
        const head = []

        // SEO优化
        if (pageData.frontmatter.description) {
            head.push(['meta', {name: 'description', content: pageData.frontmatter.description}])
        }

        // Open Graph
        head.push(['meta', {property: 'og:title', content: pageData.title || 'Qing项目文档'}])
        head.push(['meta', {
            property: 'og:description',
            content: pageData.frontmatter.description || '青（Qing）项目文档'
        }])
        head.push(['meta', {property: 'og:type', content: 'website'}])
        head.push(['meta', {
            property: 'og:url',
            content: `https://docs.qing.dev${pageData.relativePath.replace(/\.md$/, '.html')}`
        }])

        // Twitter Card
        head.push(['meta', {name: 'twitter:card', content: 'summary_large_image'}])
        head.push(['meta', {name: 'twitter:title', content: pageData.title || 'Qing项目文档'}])
        head.push(['meta', {
            name: 'twitter:description',
            content: pageData.frontmatter.description || '青（Qing）项目文档'
        }])

        return head
    },
    markdown: {
        math: true,
        image: {
            lazyLoading: true
        },
        anchor: {
            permalink: true,
            permalinkBefore: true,
            permalinkSymbol: '#'
        },
        lineNumbers: true,
        container: {
            tipLabel: '提示',
            warningLabel: '警告',
            dangerLabel: '危险',
            infoLabel: '信息',
            detailsLabel: '详细信息'
        },
        theme: {
            light: 'github-light',
            dark: 'github-dark'
        },
        config: (md) => {
            // 添加代码块复制功能和其他插件
            // 注意：这些插件需要在package.json中安装对应依赖
            // md.use(require('markdown-it-footnote'))
            // md.use(require('markdown-it-task-lists'))
            // md.use(require('markdown-it-mark'))
        }
    },
    vite: {
        optimizeDeps: {
            exclude: ['vitepress']
        },
        ssr: {
            noExternal: ['vitepress']
        },
        build: {
            chunkSizeWarningLimit: 1000,
            rollupOptions: {
                output: {
                    manualChunks: {
                        // 移除vue和vue-router，因为它们在VitePress中被视为外部模块
                        // 'vue-vendor': ['vue', 'vue-router'],
                        // 'vitepress-vendor': ['vitepress']
                    }
                }
            }
        },
        server: {
            fs: {
                allow: ['..', '../..']
            }
        }
    },
    // PWA配置（需要安装vite-plugin-pwa插件）
    // pwa: {
    //     outDir: '.vitepress/dist',
    //     registerType: 'autoUpdate',
    //     includeAssets: ['favicon.png', 'logo-mini.svg'],
    //     manifest: {
    //         name: 'Qing项目文档',
    //         short_name: 'Qing文档',
    //         description: '青（Qing）项目官方文档',
    //         theme_color: '#646cff',
    //         background_color: '#ffffff',
    //         display: 'standalone',
    //         icons: [
    //             {
    //                 src: '/favicon.png',
    //                 sizes: '192x192',
    //                 type: 'image/png'
    //             }
    //         ]
    //     },
    //     workbox: {
    //         globPatterns: ['**/*.{js,css,html,png,svg,ico,txt,woff2}']
    //     }
    // }
})
