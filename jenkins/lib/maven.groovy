#!/usr/bin/env groovy

/**
 * Maven 构建通用函数
 * 通过 evaluate(readTrusted()) 方式加载，无需 Jenkins 配置
 *
 * 使用 artifactId 格式来指定模块，避免路径问题
 */

def validate(String moduleName, String mavenOpts) {
    sh """
        pwd && ls -la
        mvn ${mavenOpts} validate \
        -pl :${moduleName} \
        -am \
        -f pom.xml
    """
}

def install(String moduleName, String mavenOpts, boolean skipTests = true) {
    def testSkip = skipTests ? "-DskipTests=true -Dmaven.test.skip=true" : ""
    sh """
        mvn ${mavenOpts} clean install \
        -pl :${moduleName} \
        -am \
        ${testSkip} \
        -f pom.xml
    """
}

def compile(String moduleName, String mavenOpts) {
    sh """
        mvn ${mavenOpts} compile \
        -pl :${moduleName} \
        -am \
        -f pom.xml
    """
}

def test(String moduleName, String mavenOpts) {
    sh """
        mvn ${mavenOpts} test \
        -pl :${moduleName} \
        -am \
        -f pom.xml
    """
}

def checkstyle(String moduleName, String mavenOpts) {
    sh """
        mvn ${mavenOpts} checkstyle:check \
        -pl :${moduleName} \
        -am \
        -f pom.xml || true
    """
}

def mavenPackage(String moduleName, String mavenOpts, String buildType) {
    def command = buildType == 'release' ? 'clean package' : 'package'
    sh """
        mvn ${mavenOpts} ${command} \
        -pl :${moduleName} \
        -am \
        -DskipTests=true \
        -f pom.xml
    """
}

return this
