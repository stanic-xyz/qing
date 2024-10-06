import {defineConfig} from 'vitepress'

// https://vitepress.dev/reference/site-config
export default defineConfig({
    base: "/qing",
    title: "Qing",
    description: "项目文档",
    themeConfig: {
        // https://vitepress.dev/reference/default-theme-config
        nav: [
            {text: '首页', link: '/'},
            {text: '示例', link: '/markdown-examples'},
            {text: '设计', link: '/design/动漫管理网站'}
        ],
        sidebar: [
            {
                text: '简介',
                collapsed: false,
                link: '/introduction'
            },
            {
                text: '用户指南',
                collapsed: false,
                items: [
                    {
                        text: 'Markdown 示例',
                        collapsed: true,
                        items: [
                            {
                                text: 'Markdown Examples', link: '/markdown-examples'
                            }
                        ]
                    },
                    {text: '代码生成器', link: '/代码生成器/readme.md'},
                    {
                        text: '设计文档',
                        collapsed: true,
                        items: [
                            {text: '总体设计', link: '/design/动漫管理网站'},
                            {text: '数据库设计', link: '/db/readme'},
                        ]
                    },
                ]
            },
            {
                text: '关于文档',
                collapsed: false,
                link: '/category/关于文档'
            },
            {
                text: '博客',
                collapsed: true,
                items: [
                    {text: 'K3s', link: '/blog/K3s环境使用Let‘s Encrypt证书的部署及自动配置https域名-阿里云域名解析管理'},
                    {text: '安装Ubuntu', link: '/blog/在Ubuntu中安装PostgreSql并配置远程访问'},
                    {text: '安装Harbor', link: '/blog/安装Harbor'},
                    {text: 'Docker镜像', link: '/blog/搭建Docker镜像加速'}
                ]
            },
        ],
        socialLinks: [
            {icon: 'github', link: 'https://github.com/stanic-xyz/qing'}
        ],
        footer: {
            message: '在 Mulan PSL v2 协议下开源，请遵守开源协议.',
            copyright: 'Copyright © 2018-2024 stanic.xyz'
        }
    },
    sitemap: {
        hostname: 'https://docs.stanic.xyz'
    },
    lastUpdated: true,
    ignoreDeadLinks: true,
})
