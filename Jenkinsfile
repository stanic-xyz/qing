pipeline {
    agent any
    stages {
        stage('阶段-1 拉取代码') {
            steps {
                checkout([
                        $class           : "GitSCM",
                        branches         : [[name: env.GIT_BUILD_REF]],
                        userRemoteConfigs: [[url: env.GIT_REPO_URL, credentialsId: env.CREDENTIALS_ID]]])
            }
        }
        stage('阶段-2 单元测试') {
            steps {
                sh "mvn -pl zhangli-service-provider test"
            }
            post {
                always {
                    // 收集测试报告
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }
        stage('阶段-3 打包') {
            steps {
                script {
                    sh "mvn  -pl zhangli-service-provider clean package dockerfile:build dockerfile:tag dockerfile:push -DskipTests=true"
                }
            }
        }
    }
}