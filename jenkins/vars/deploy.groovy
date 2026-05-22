#!/usr/bin/env groovy

/**
 * Kubernetes 部署步骤
 *
 * 使用方式:
 * deploy(imageTag: '1', serviceName: 'llm')
 */
def call(Map config) {
    def imageTag = config.imageTag ?: 'latest'
    def serviceName = config.serviceName
    def registryUrl = config.registryUrl ?: 'your-registry'
    def namespace = config.namespace ?: 'default'

    echo "开始部署到生产环境: ${serviceName}"

    echo "1. 推送镜像到仓库"
    sh """
        docker push ${registryUrl}/${serviceName}:${imageTag}
        docker push ${registryUrl}/${serviceName}:latest
    """

    echo "2. 更新 Kubernetes 部署"
    sh """
        export IMAGE_TAG=${imageTag}
        export SERVICE_NAME=${serviceName}
        export REGISTRY_URL=${registryUrl}
        export NAMESPACE=${namespace}
        envsubst < jenkins/resources/deployment-template.yaml | kubectl apply -n ${namespace} -f -
    """

    echo "3. 等待部署完成"
    sh """
        kubectl rollout status deployment/${serviceName} -n ${namespace} --timeout=300s || true
    """
}

/**
 * 获取服务状态
 */
def getStatus(Map config) {
    def serviceName = config.serviceName
    def namespace = config.namespace ?: 'default'

    sh """
        kubectl get svc ${serviceName} -n ${namespace}
        kubectl get pods -n ${namespace} -l app=${serviceName}
    """
}

/**
 * 回滚到上一个版本
 */
def rollback(Map config) {
    def serviceName = config.serviceName
    def namespace = config.namespace ?: 'default'

    echo "回滚部署: ${serviceName}"
    sh """
        kubectl rollout undo deployment/${serviceName} -n ${namespace}
    """
}
