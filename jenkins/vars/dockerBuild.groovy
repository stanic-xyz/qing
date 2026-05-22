#!/usr/bin/env groovy

/**
 * Docker 镜像构建步骤
 *
 * 使用方式:
 * dockerBuild(moduleName: 'qing-service-llm', buildNumber: '1')
 */
def call(Map config) {
    def moduleName = config.moduleName
    def buildNumber = config.buildNumber ?: 'latest'
    def registryUrl = config.registryUrl ?: 'your-registry'

    def serviceName = moduleName.replace('qing-service-', '')
    def dockerfilePath = "qing-services/${moduleName}/Dockerfile"
    def modulePath = "qing-services/${moduleName}"

    echo "开始构建 Docker 镜像: ${serviceName}"

    sh """
        docker build \
        -t ${registryUrl}/${serviceName}:${buildNumber} \
        -t ${registryUrl}/${serviceName}:latest \
        -f ${dockerfilePath} \
        ${modulePath}/
    """

    return [
        imageName: "${registryUrl}/${serviceName}:${buildNumber}",
        serviceName: serviceName
    ]
}
