#!/usr/bin/env groovy

/**
 * 通用模块构建步骤
 * 用于构建指定的 Maven 模块及其依赖
 *
 * 使用方式:
 * buildModule(moduleName: 'qing-service-llm', buildType: 'release')
 */
def call(Map config) {
    def moduleName = config.moduleName
    def buildType = config.buildType ?: 'debug'
    def skipTests = config.skipTests ?: false

    def mavenOpts = "--no-transfer-progress --batch-mode --errors --fail-at-end --show-version"

    pipeline {
        stages {
            stage('验证依赖完整性') {
                steps {
                    script {
                        echo "验证项目依赖: ${moduleName}"
                        sh """
                            mvn ${mavenOpts} validate \
                            -pl qing-services/${moduleName} \
                            -am \
                            -f pom.xml
                        """
                    }
                }
            }

            stage('安装基础依赖包') {
                steps {
                    script {
                        echo "安装基础依赖: ${moduleName}"
                        sh """
                            mvn ${mavenOpts} clean install \
                            -pl qing-services/${moduleName} \
                            -am \
                            -DskipTests=true \
                            -Dmaven.test.skip=true \
                            -f pom.xml
                        """
                    }
                }
            }

            stage('编译核心模块') {
                steps {
                    script {
                        echo "编译核心模块: ${moduleName}"
                        sh """
                            mvn ${mavenOpts} compile \
                            -pl qing-services/${moduleName} \
                            -am \
                            -f pom.xml
                        """
                    }
                }
            }

            stage('单元测试') {
                when {
                    expression { !skipTests }
                }
                steps {
                    script {
                        echo "执行单元测试: ${moduleName}"
                        sh """
                            mvn ${mavenOpts} test \
                            -pl qing-services/${moduleName} \
                            -am \
                            -f pom.xml
                        """
                    }
                }
                post {
                    always {
                        junit "qing-services/${moduleName}/target/surefire-reports/*.xml"
                    }
                    failure {
                        echo "单元测试失败，请检查测试用例"
                        archiveArtifacts artifacts: "qing-services/${moduleName}/target/surefire-reports/*.txt, qing-services/${moduleName}/target/surefire-reports/*.xml"
                    }
                }
            }

            stage('代码质量检查') {
                steps {
                    script {
                        echo "代码质量检查: ${moduleName}"
                        sh """
                            mvn ${mavenOpts} checkstyle:check \
                            -pl qing-services/${moduleName} \
                            -am \
                            -f pom.xml || true
                        """
                    }
                }
            }

            stage('编译打包') {
                steps {
                    script {
                        echo "编译打包: ${moduleName}"
                        def mavenCommand = 'package'
                        if (buildType == 'release') {
                            mavenCommand = 'clean package'
                            echo "执行 Release 模式构建"
                        } else {
                            echo "执行 Debug 模式构建"
                        }

                        sh """
                            mvn ${mavenOpts} ${mavenCommand} \
                            -pl qing-services/${moduleName} \
                            -am \
                            -DskipTests=true \
                            -f pom.xml
                        """
                    }
                }
                post {
                    success {
                        echo "编译打包成功"
                        archiveArtifacts artifacts: "qing-services/${moduleName}/target/*.jar", fingerprint: true

                        script {
                            def jarFile = findFiles(glob: "qing-services/${moduleName}/target/*.jar")[0]
                            if (jarFile != null) {
                                echo "生成的 JAR 文件: ${jarFile.name}"
                                echo "文件大小: ${jarFile.length()} bytes"
                            }
                        }
                    }
                    failure {
                        echo "编译打包失败"
                    }
                }
            }
        }
    }
}
