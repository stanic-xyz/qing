#!/usr/bin/env groovy

/**
 * Kubernetes 部署通用函数
 * 通过 evaluate(readTrusted()) 方式加载，无需 Jenkins 配置
 */

def deploy(String serviceName, String registryUrl, String buildNumber, String namespace = "default") {
    echo "部署到生产环境: ${serviceName}"

    sh """
        export IMAGE_TAG=${buildNumber}
        export SERVICE_NAME=${serviceName}
        export REGISTRY_URL=${registryUrl}
        export NAMESPACE=${namespace}
        envsubst < jenkins/resources/deployment-template.yaml | kubectl apply -n ${namespace} -f -
    """

    waitForRollout(serviceName, namespace)
}

def waitForRollout(String serviceName, String namespace = "default") {
    echo "等待部署完成..."
    sh """
        kubectl rollout status deployment/${serviceName} -n ${namespace} --timeout=300s || true
    """
}

def rollback(String serviceName, String namespace = "default") {
    echo "回滚部署: ${serviceName}"
    sh """
        kubectl rollout undo deployment/${serviceName} -n ${namespace}
    """
}

def getStatus(String serviceName, String namespace = "default") {
    echo "获取服务状态: ${serviceName}"
    sh """
        kubectl get svc ${serviceName} -n ${namespace}
        kubectl get pods -n ${namespace} -l app=${serviceName}
    """
}

return this
