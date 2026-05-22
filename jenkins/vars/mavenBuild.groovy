#!/usr/bin/env groovy

/**
 * Maven 通用构建步骤
 * 注意：这是用于文档说明的占位文件
 * 实际函数定义在各服务的 Jenkinsfile 中
 *
 * 如需复用这些步骤，建议：
 * 1. 将公共代码提取到单独的 .groovy 文件
 * 2. 使用 evaluate() 或 readTrusted() 动态加载
 */

/**
 * Maven 构建通用参数
 */
def getMavenOpts() {
    return "--no-transfer-progress --batch-mode --errors --fail-at-end --show-version"
}

/**
 * 获取模块路径
 */
def getModulePath(moduleName) {
    return "qing-services/${moduleName}"
}
