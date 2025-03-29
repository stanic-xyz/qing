pipeline {
    agent {
        label 'maven-agent'
    }
    tools {
        // 配置 JDK 和 Maven 版本
        maven 'Maven-3.8.4'
        jdk 'JDK-1.8'
    }
    options {
        // 保留最近 10 次构建记录
        buildDiscarder(logRotator(numToKeepStr: '10'))
        // 构建超时时间为 30 分钟
        timeout(time: 30, unit: 'MINUTES')
    }
    environment {
        // 定义 Maven 本地仓库路径
        MAVEN_LOCAL_REPO = "${JENKINS_HOME}/.m2/repository"
    }
    stages {
        stage('安装基础依赖包') {
            steps {
                sh '''echo 安装基础依赖包 Security Starter'''
                sh '''mvn clean install -pl qing-starters/qing-security-spring-boot-starter -am -f pom.xml'''
                sh '''echo 安装基础依赖包 infrastructure'''
                sh '''mvn clean install -pl qing-infrastructure -am -f pom.xml'''
                sh '''echo 安装代码生成器'''
                sh '''mvn clean install -pl qing-codegen-plugin/qing-codegen-apt -am -f pom.xml'''
            }
        }
        stage('单元测试') {
            steps {
                sh "mvn test -pl qing-domain -f pom.xml"
            }
            post {
                always {
                    // 收集测试报告
                    junit 'qing-domain/target/surefire-reports/*.xml'
                }
                failure {
                    // 单元测试失败时发送通知
                    mail to: 'your-email@example.com',
                        subject: "构建失败: ${JOB_NAME} #${BUILD_NUMBER}",
                        body: "在单元测试阶段失败，请检查日志: ${BUILD_URL}"
                }
            }
        }
        stage('编译打包 Domain') {
            steps {
                sh '''
                    echo 开始编译
                    mvn clean package -pl qing-domain -f pom.xml -DskipTests=true
                '''
            }
            post {
                success {
                    // 编译打包成功后归档生成的工件
                    archiveArtifacts artifacts: 'qing-domain/target/*.jar'
                }
                failure {
                    // 编译打包失败时发送通知
                    mail to: 'your-email@example.com',
                        subject: "构建失败: ${JOB_NAME} #${BUILD_NUMBER}",
                        body: "在编译打包 Domain 阶段失败，请检查日志: ${BUILD_URL}"
                }
            }
        }
        stage('编译打包 Domain') {
            steps {
                sh '''echo 开始编译'''
                sh '''mvn clean package -pl qing-domain -f pom.xml -DskipTests=true'''
            }
        }
        stage('部署到生产环境') {
            steps {
                // 这里可以添加部署到生产环境的脚本，例如使用 Docker 部署
                sh '''
                    echo 开始部署到生产环境
                    # 示例：使用 Docker 部署
                    docker build -t your-image-name:${BUILD_NUMBER} .
                    docker push your-image-name:${BUILD_NUMBER}
                    kubectl apply -f deployment.yaml
                '''
            }
            post {
                success {
                    // 部署成功后发送通知
                    mail to: 'your-email@example.com',
                        subject: "部署成功: ${JOB_NAME} #${BUILD_NUMBER}",
                        body: "已成功部署到生产环境: ${BUILD_URL}"
                }
                failure {
                    // 部署失败时发送通知
                    mail to: 'your-email@example.com',
                        subject: "部署失败: ${JOB_NAME} #${BUILD_NUMBER}",
                        body: "在部署到生产环境阶段失败，请检查日志: ${BUILD_URL}"
                }
            }
        }
    }
}
