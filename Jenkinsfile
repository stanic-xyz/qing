pipeline {
    agent {
        label 'local'
    }

    options {
        skipDefaultCheckout(false)
        disableConcurrentBuilds()
        buildDiscarder(logRotator(numToKeepStr: '10'))
        timeout(time: 20, unit: 'MINUTES')
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
                    'qing-services/qing-service-auth',
                    'qing-services/qing-service-llm',
                    'qing-services/qing-service-anime',
                    'qing-services/qing-service-leave',
                    'qing-services/qing-service-minimall',
                    'qing-services/qing-service-workflow'
                ],
                description: '选择要构建的服务模块'
        )
    }

    environment {
        // 定义 Maven 本地仓库路径
        MAVEN_LOCAL_REPO = "${JENKINS_HOME}/.m2/repository"
        MAVEN_CLI_OPTS = "--no-transfer-progress --batch-mode --errors --fail-at-end --show-version"
        // 可选：设置阿里云镜像加速
        MAVEN_MIRROR_URL = "https://maven.aliyun.com/repository/public"
    }

    stages {
        stage('拉取项目代码') {
            steps {
                checkout scm
            }
        }

        stage('代码质量检查') {
            steps {
                script {
                    echo "进行代码质量检查: ${params.SERVICE_MODULE}"
                    // 可选：运行静态代码分析
                    sh """
                        mvn ${env.MAVEN_CLI_OPTS} checkstyle:check \
                        -pl ${params.SERVICE_MODULE} \
                        -am \
                        -f pom.xml
                    """
                }
            }
        }

        stage('编译核心模块') {
            steps {
                script {
                    echo "开始编译核心模块: ${params.SERVICE_MODULE}"
                    sh """
                        mvn ${env.MAVEN_CLI_OPTS} compile \
                        -pl ${params.SERVICE_MODULE} \
                        -am \
                        -f pom.xml
                    """
                }
            }
        }

        stage('单元测试') {
            steps {
                script {
                    echo "开始执行单元测试: ${params.SERVICE_MODULE}"
                    sh """
                        mvn ${env.MAVEN_CLI_OPTS} test \
                        -pl ${params.SERVICE_MODULE} \
                        -am \
                        -f pom.xml
                    """
                }
            }
            post {
                always {
                    // 收集测试报告
                    junit "${params.SERVICE_MODULE}/target/surefire-reports/*.xml"

                    // 可选：生成测试覆盖率报告
                    // jacoco(
                    //     execPattern: '**/jacoco.exec',
                    //     classPattern: '**/classes',
                    //     sourcePattern: '**/src/main/java'
                    // )
                }
                failure {
                    echo "单元测试失败，请检查测试用例"
                    // 保存测试日志
                    archiveArtifacts artifacts: "${params.SERVICE_MODULE}/target/surefire-reports/*.txt, ${params.SERVICE_MODULE}/target/surefire-reports/*.xml"
                }
            }
        }

        stage('编译打包') {
            steps {
                script {
                    echo "开始编译打包: ${params.SERVICE_MODULE}"

                    // 根据构建类型选择不同的命令
                    def mavenCommand = 'package'
                    if (params.BUILD_TYPE == 'release') {
                        mavenCommand = 'clean package'
                        echo "执行Release模式构建"
                    } else {
                        echo "执行Debug模式构建"
                    }

                    sh """
                        mvn ${env.MAVEN_CLI_OPTS} ${mavenCommand} \
                        -pl ${params.SERVICE_MODULE} \
                        -am \
                        -DskipTests=true \
                        -f pom.xml
                    """
                }
            }
            post {
                success {
                    echo "编译打包成功"
                    // 归档生成的工件
                    archiveArtifacts artifacts: "${params.SERVICE_MODULE}/target/*.jar", fingerprint: true

                    // 可选：记录构建信息
                    script {
                        def jarFile = findFiles(glob: "${params.SERVICE_MODULE}/target/*.jar")[0]
                        if (jarFile != null) {
                            echo "生成的JAR文件: ${jarFile.name}"
                            echo "文件大小: ${jarFile.length()} bytes"
                        }
                    }
                }
                failure {
                    echo "编译打包失败"
                }
            }
        }

        stage('构建Docker镜像') {
            when {
                expression { params.BUILD_TYPE == 'release' }
            }
            steps {
                script {
                    def serviceName = params.SERVICE_MODULE.split('/')[-1]
                    echo "开始构建Docker镜像: ${serviceName}"

                    // 动态获取Dockerfile路径
                    def dockerfilePath = "${params.SERVICE_MODULE}/Dockerfile"
                    def modulePath = params.SERVICE_MODULE

                    // 假设你的项目中有Dockerfile
                    sh """
                        docker build \
                        -t your-registry/${serviceName}:${BUILD_NUMBER} \
                        -t your-registry/${serviceName}:latest \
                        -f ${dockerfilePath} \
                        ${modulePath}/
                    """
                }
            }
        }

        stage('部署到生产环境') {
            when {
                expression { params.BUILD_TYPE == 'release' }
            }
            steps {
                script {
                    def serviceName = params.SERVICE_MODULE.split('/')[-1]
                    echo "开始部署到生产环境: ${serviceName}"

                    // 1. 推送到镜像仓库
                    sh """
                        docker push your-registry/${serviceName}:${BUILD_NUMBER}
                        docker push your-registry/${serviceName}:latest
                    """

                    // 2. 更新Kubernetes部署
                    sh """
                        # 使用envsubst替换部署文件中的变量
                        export IMAGE_TAG=${BUILD_NUMBER}
                        export SERVICE_NAME=${serviceName}
                        envsubst < deployment.yaml | kubectl apply -f -

                        # 可选：等待部署完成
                        kubectl rollout status deployment/${serviceName} --timeout=300s
                    """
                }
            }
            post {
                success {
                    echo "部署成功"
                    script {
                        def serviceName = params.SERVICE_MODULE.split('/')[-1]
                        // 获取服务访问信息
                        sh """
                            kubectl get svc ${serviceName}
                            kubectl get pods -l app=${serviceName}
                        """
                    }
                }
                failure {
                    echo "部署失败，将尝试回滚"
                    script {
                        def serviceName = params.SERVICE_MODULE.split('/')[-1]
                        // 自动回滚到上一个版本
                        sh """
                            kubectl rollout undo deployment/${serviceName}
                        """
                    }
                }
            }
        }
    }

    post {
        always {
            echo "构建完成，清理工作空间..."
            // 清理不需要的文件
            // 生成构建报告
            script {
                def serviceName = params.SERVICE_MODULE.split('/')[-1]
                currentBuild.description = "模块: ${serviceName}, 构建类型: ${params.BUILD_TYPE}, 状态: ${currentBuild.result}"
            }
        }
        success {
            echo "构建成功！"
            // 可选：发送成功通知
            // emailext (
            //     to: 'your-email@example.com',
            //     subject: "构建成功: ${JOB_NAME} #${BUILD_NUMBER}",
            //     body: "构建成功，详情请查看: ${BUILD_URL}"
            // )
        }
        failure {
            echo "构建失败！"
            // 失败时发送详细通知
            // emailext (
            //     to: 'your-email@example.com',
            //     subject: "构建失败: ${JOB_NAME} #${BUILD_NUMBER}",
            //     body: """
            //         构建失败，请检查：
            //         项目: ${JOB_NAME}
            //         构建号: ${BUILD_NUMBER}
            //         构建URL: ${BUILD_URL}
            //         失败阶段: ${currentBuild.currentResult}
            //     """
            // )
        }
        unstable {
            echo "构建不稳定，请检查测试结果"
        }
    }
}
