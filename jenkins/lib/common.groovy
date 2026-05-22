#!/usr/bin/env groovy

/**
 * 通用工具函数
 * 通过 evaluate(readTrusted()) 方式加载，无需 Jenkins 配置
 */

static def getMavenOpts() {
    return "--no-transfer-progress --batch-mode --errors --fail-at-end --show-version"
}

static def getServiceName(String moduleName) {
    return moduleName.replace('qing-service-', '')
}

static def getModulePath(String moduleName) {
    return moduleName
}

def printBuildInfo(Map config) {
    echo """
    ==========================================
      Qing 项目 CI/CD 流水线
    ==========================================
      服务: ${config.serviceName}
      模块: ${config.moduleName}
      类型: ${config.buildType}
      编号: ${config.buildNumber}
    ==========================================
    """
}

def archiveJar(String modulePath) {
    echo "Archiving jar filepath... ${modulePath}"
    archiveArtifacts artifacts: "qing-services/${modulePath}/target/*.jar", fingerprint: true
}

return this
