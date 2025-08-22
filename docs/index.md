---
# https://vitepress.dev/reference/default-theme-home-page
layout: home

hero:
  name: "Qing"
  text: "现代化微服务开发平台"
  tagline: "基于领域驱动设计，让复杂业务变得简单优雅 ✨"
  actions:
    - theme: brand
      text: 快速开始
      link: /category/文档
    - theme: alt
      text: 接口说明
      link: /api-examples
    - theme: alt
      text: 开发指南
      link: /category/开发者指南/
  image:
    src: /logo-large.webp
    alt: Qing

features:
  - icon: 🏗️
    title: 领域驱动设计
    details: 遵循DDD最佳实践，让业务逻辑清晰可维护，代码结构优雅有序
  - icon: ⚡
    title: 智能代码生成
    details: 告别重复劳动！一键生成标准化代码，让开发者专注于业务创新
  - icon: 🚀
    title: 现代技术栈
    details: Spring Boot + Jimmer + Vue3，为您提供企业级开发体验
  - icon: 🔧
    title: 开箱即用
    details: 完整的微服务解决方案，从项目初始化到生产部署，一站式搞定
---

<style>
:root {
  --vp-home-hero-name-color: transparent;
  --vp-home-hero-name-background: -webkit-linear-gradient(120deg, #bd34fe 30%, #41d1ff);

  --vp-home-hero-image-background-image: linear-gradient(-45deg, #bd34fe 50%, #47caff 50%);
  --vp-home-hero-image-filter: blur(44px);


  --vp-home-hero-tagline-color: transparent;
  --vp-home-hero-tagline-background: -webkit-linear-gradient(120deg, #bd34fe 30%, #41d1ff);

  --vp-home-hero-tagline-background-image: linear-gradient(-45deg, #bd34fe 50%, #47caff 50%);
  --vp-home-hero-tagline-filter: blur(44px);
}

@media (min-width: 640px) {
  :root {
    --vp-home-hero-image-filter: blur(56px);
  }
}

@media (min-width: 960px) {
  :root {
    --vp-home-hero-image-filter: blur(68px);
  }
}
</style>

## 项目理念

**Qing** 诞生于对优雅代码的追求和对开发效率的思考。我们深知企业级应用开发的复杂性，也理解开发者对于工具选择的谨慎。

这是一个专注于**领域驱动设计**的现代化开发框架，融合了**Spring Cloud**生态的成熟稳定与**代码生成**
技术的高效便捷。我们不追求大而全，而是致力于在关键环节提供精准的解决方案。

每一行代码都经过深思熟虑，每一个设计决策都源于实际项目的历练。我们相信，真正优秀的框架应该让开发者专注于业务创新，而非重复的基础工作。

**Qing** 期待与志同道合的开发者一起，构建更加优雅、高效的企业级应用。
