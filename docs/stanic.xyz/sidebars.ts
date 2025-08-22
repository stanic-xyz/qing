import type {SidebarsConfig} from '@docusaurus/plugin-content-docs';

/**
 * Creating a sidebar enables you to:
 - create an ordered group of docs
 - render a sidebar for each doc of that group
 - provide next/previous navigation

 The sidebars can be generated from the filesystem, or explicitly defined here.

 Create as many sidebars as you want.
 */
const sidebars: SidebarsConfig = {
    // 手动定义侧边栏结构，提供更好的导航体验
    tutorialSidebar: [
        'intro',
        {
            type: 'category',
            label: '基础教程',
            items: [
                'tutorial-basics/getting-started',
                'tutorial-basics/user-guide',
                'tutorial-basics/deployment',
            ],
        },
        {
            type: 'category',
            label: '进阶指南',
            items: [
                'tutorial-extras/development',
                'tutorial-extras/api-docs',
                'tutorial-extras/architecture',
                'tutorial-extras/faq',
            ],
        },
    ],
};

export default sidebars;
