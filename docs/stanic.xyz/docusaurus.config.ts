import {themes as prismThemes} from 'prism-react-renderer';
import type {Config} from '@docusaurus/types';
import type * as Preset from '@docusaurus/preset-classic';

const config: Config = {
    title: '青（Qing）',
    tagline: '现代化微服务架构的动漫管理平台 🎌',
    favicon: 'img/favicon.ico',

    // Set the production url of your site here
    url: 'https://docs.stanic.xyz',
    // Set the /<baseUrl>/ pathname under which your site is served
    // For GitHub pages deployment, it is often '/<projectName>/'
    baseUrl: '/',

    // GitHub pages deployment config.
    // If you aren't using GitHub pages, you don't need these.
    organizationName: 'stanic-xyz', // Usually your GitHub org/user name.
    projectName: 'qing', // Usually your repo name.

    onBrokenLinks: 'throw',
    onBrokenMarkdownLinks: 'warn',

    // Even if you don't use internationalization, you can use this field to set
    // useful metadata like html lang. For example, if your site is Chinese, you
    // may want to replace "en" with "zh-Hans".
    i18n: {
        defaultLocale: 'zh-Hans',
        locales: ['zh-Hans'],
    },

    presets: [
        [
            'classic',
            {
                docs: {
                    sidebarPath: './sidebars.ts',
                    // Please change this to your repo.
                    // Remove this to remove the "edit this page" links.
                    editUrl:
                        'https://github.com/stanic-xyz/qing/tree/main/docs/stanic.xyz/',
                },
                blog: {
                    showReadingTime: true,
                    // Please change this to your repo.
                    // Remove this to remove the "edit this page" links.
                    editUrl:
                        'https://github.com/stanic-xyz/qing/tree/main/docs/stanic.xyz/',
                },
                theme: {
                    customCss: './src/css/custom.css',
                },
            } satisfies Preset.Options,
        ],
    ],

    themeConfig: {
        // Replace with your project's social card
        image: 'img/docusaurus-social-card.jpg',
        navbar: {
            title: '青（Qing）',
            logo: {
                alt: 'Qing Logo',
                src: 'img/logo.svg',
            },
            items: [
                {
                    type: 'docSidebar',
                    sidebarId: 'tutorialSidebar',
                    position: 'left',
                    label: '文档',
                },
                {to: '/blog', label: '博客', position: 'left'},
                {
                    href: 'https://github.com/stanic-xyz/qing',
                    label: 'GitHub',
                    position: 'right',
                },
            ],
        },
        footer: {
            style: 'dark',
            links: [
                {
                    title: '文档',
                    items: [
                        {
                            label: '快速开始',
                            to: '/docs/tutorial-basics/getting-started',
                        },
                        {
                            label: '用户指南',
                            to: '/docs/tutorial-basics/user-guide',
                        },
                        {
                            label: 'API 文档',
                            to: '/docs/tutorial-extras/api-docs',
                        },
                    ],
                },
                {
                    title: '社区',
                    items: [
                        {
                            label: 'GitHub Issues',
                            href: 'https://github.com/stanic-xyz/qing/issues',
                        },
                        {
                            label: 'GitHub Discussions',
                            href: 'https://github.com/stanic-xyz/qing/discussions',
                        },
                        {
                            label: 'Gitee（镜像）',
                            href: 'https://gitee.com/stanic/qing',
                        },
                    ],
                },
                {
                    title: '更多',
                    items: [
                        {
                            label: '博客',
                            to: '/blog',
                        },
                        {
                            label: 'GitHub',
                            href: 'https://github.com/stanic-xyz/qing',
                        },
                        {
                            label: '开源协议',
                            href: 'https://github.com/stanic-xyz/qing/blob/main/LICENSE.TXT',
                        },
                    ],
                },
            ],
            copyright: `Copyright © ${new Date().getFullYear()} 青（Qing）项目. 基于木兰宽松许可证第2版开源.`,
        },
        prism: {
            theme: prismThemes.github,
            darkTheme: prismThemes.dracula,
        },
    } satisfies Preset.ThemeConfig,
};

export default config;
