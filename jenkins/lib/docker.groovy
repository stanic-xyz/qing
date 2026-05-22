#!/usr/bin/env groovy

/**
 * Docker 构建通用函数
 * 通过 evaluate(readTrusted()) 方式加载，无需 Jenkins 配置
 *
 * 注意：moduleName 应该是完整的模块路径，如 "qing-service-auth"
 */

def build(String serviceName, String moduleName, String registryUrl, String buildNumber) {
    echo "构建 Docker 镜像: ${serviceName}"
    sh """
        docker build \
        -t ${registryUrl}/${serviceName}:${buildNumber} \
        -t ${registryUrl}/${serviceName}:latest \
        -f ${moduleName}/Dockerfile \
        ${moduleName}/
    """
}

def push(String serviceName, String registryUrl, String buildNumber) {
    echo "推送镜像到仓库: ${serviceName}"
    sh """
        docker push ${registryUrl}/${serviceName}:${buildNumber}
        docker push ${registryUrl}/${serviceName}:latest
    """
}

def buildAndPush(String serviceName, String moduleName, String registryUrl, String buildNumber) {
    build(serviceName, moduleName, registryUrl, buildNumber)
    push(serviceName, registryUrl, buildNumber)
}

return this
