/**
 * Qing 项目 CI/CD 流水线入口
 *
 * 完全代码化：无需在 Jenkins 中配置任何共享库
 * 共享库通过 evaluate(readTrusted()) 从代码库加载
 */

// ==================== 加载共享库 ====================

def common = evaluate(readTrusted('jenkins/lib/common.groovy'))
def maven = evaluate(readTrusted('jenkins/lib/maven.groovy'))
def docker = evaluate(readTrusted('jenkins/lib/docker.groovy'))
def k8s = evaluate(readTrusted('jenkins/lib/kubernetes.groovy'))

// ==================== 主流水线 ====================

pipeline {
    agent {
        label 'local'
    }

    options {
        skipDefaultCheckout(false)
        disableConcurrentBuilds()
        buildDiscarder(logRotator(numToKeepStr: '10'))
        timeout(time: 30, unit: 'MINUTES')
        retry(0)
    }

    parameters {
        choice(
            name: 'BUILD_TYPE',
            choices: ['debug', 'release'],
            description: '选择构建类型'
        )
        choice(
            name: 'SERVICE_MODULE',
            choices: [
                'qing-service-auth',
                'qing-service-llm',
                'qing-service-anime',
                'qing-service-leave',
                'qing-service-minimall',
                'qing-service-workflow'
            ],
            description: '选择要构建的服务模块'
        )
    }

    environment {
        REGISTRY_URL = 'your-registry'
    }

    stages {
        stage('拉取项目代码') {
            steps {
                checkout scm
            }
        }

        stage('显示构建信息') {
            steps {
                script {
                    def serviceName = common.getServiceName(params.SERVICE_MODULE)
                    common.printBuildInfo([
                        serviceName: serviceName,
                        moduleName: params.SERVICE_MODULE,
                        buildType: params.BUILD_TYPE,
                        buildNumber: env.BUILD_NUMBER
                    ])
                }
            }
        }

        stage('验证依赖完整性') {
            steps {
                script {
                    echo "验证项目依赖: ${params.SERVICE_MODULE}..."
                    maven.validate(params.SERVICE_MODULE, common.getMavenOpts())
                }
            }
        }

        stage('安装基础依赖包') {
            steps {
                script {
                    echo "安装基础依赖: ${params.SERVICE_MODULE}..."
                    maven.install(params.SERVICE_MODULE, common.getMavenOpts())
                }
            }
        }

        stage('编译核心模块') {
            steps {
                script {
                    echo "编译核心模块: ${params.SERVICE_MODULE}..."
                    maven.compile(params.SERVICE_MODULE, common.getMavenOpts())
                }
            }
        }

        stage('单元测试') {
            steps {
                script {
                    echo "执行单元测试: ${params.SERVICE_MODULE}..."
                    maven.test(params.SERVICE_MODULE, common.getMavenOpts())
                }
            }
            post {
                always {
                    script {
                        junit "qing-services/${params.SERVICE_MODULE}/target/surefire-reports/*.xml"
                    }
                }
                failure {
                    script {
                        echo "单元测试失败"
                        archiveArtifacts artifacts: "qing-services/${params.SERVICE_MODULE}/target/surefire-reports/*.txt, qing-services/${params.SERVICE_MODULE}/target/surefire-reports/*.xml"
                    }
                }
            }
        }

        stage('代码质量检查') {
            steps {
                script {
                    echo "代码质量检查: ${params.SERVICE_MODULE}..."
                    maven.checkstyle(params.SERVICE_MODULE, common.getMavenOpts())
                }
            }
        }

        stage('编译打包') {
            steps {
                script {
                    echo "编译打包: ${params.SERVICE_MODULE}..."

                    if (params.BUILD_TYPE == 'release') {
                        echo "执行 Release 模式构建"
                    } else {
                        echo "执行 Debug 模式构建"
                    }

                    maven.package(params.SERVICE_MODULE, common.getMavenOpts(), params.BUILD_TYPE)
                }
            }
            post {
                success {
                    script {
                        echo "编译打包成功"
                        common.archiveJar("qing-services/${params.SERVICE_MODULE}")
                    }
                }
                failure {
                    script {
                        echo "编译打包失败"
                    }
     }
            }
        }

        stage('构建 Docker 镜像') {
            when {
                expression { params.BUILD_TYPE == 'release' }
            }
            steps {
                script {
                    def serviceName = common.getServiceName(params.SERVICE_MODULE)
                    docker.build(serviceName, params.SERVICE_MODULE, env.REGISTRY_URL, env.BUILD_NUMBER)
                }
            }
        }

        stage('部署到生产环境') {
            when {
                expression { params.BUILD_TYPE == 'release' }
            }
            steps {
                script {
                    def serviceName = common.getServiceName(params.SERVICE_MODULE)
                    docker.push(serviceName, env.REGISTRY_URL, env.BUILD_NUMBER)
                    k8s.deploy(serviceName, env.REGISTRY_URL, env.BUILD_NUMBER)
                }
            }
            post {
                success {
                    script {
                        echo "部署成功"
                    }
                }
                failure {
                    script {
                        echo "部署失败，尝试回滚"
                        def serviceName = common.getServiceName(params.SERVICE_MODULE)
                        k8s.rollback(serviceName)
                    }
                }
            }
        }
    }

    post {
        always {
            script {
                def serviceName = common.getServiceName(params.SERVICE_MODULE)
                currentBuild.description = "服务: ${serviceName}, 类型: ${params.BUILD_TYPE}, 状态: ${currentBuild.result}"
            }
        }
        success {
            script {
                echo "=========================================="
                echo "  构建成功！"
                echo "=========================================="
            }
        }
        failure {
            script {
                echo "=========================================="
                echo "  构建失败！"
                echo "=========================================="
            }
        }
    }
}
