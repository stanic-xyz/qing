/**
 * Jenkins Generic Webhook Trigger 配置示例
 *
 * 在 Jenkins Job 配置中：
 * 1. 安装插件: Generic Webhook Trigger Plugin
 * 2. 在 Job 配置页面 -> 触发器 -> 勾选 "Generic Webhook Trigger"
 * 3. 设置 Token: qing-trigger
 *
 * Gitea Webhook 配置:
 * 目标 URL: http://your-jenkins-url/generic-webhook-trigger/invoke?token=qing-trigger
 *
 * 支持的触发参数:
 * - token: 触发令牌
 * - module: 指定构建的模块 (如 qing-service-llm)
 * - buildType: 构建类型 (debug/release)
 * - branch: 分支名称
 */

def call(Map config = [:]) {
    def module = config.module ?: 'qing-service-llm'
    def buildType = config.buildType ?: 'debug'
    def branch = config.branch ?: 'main'

    echo "Webhook 触发构建"
    echo "  模块: ${module}"
    echo "  类型: ${buildType}"
    echo "  分支: ${branch}"

    return [
        module: module,
        buildType: buildType,
        branch: branch
    ]
}
