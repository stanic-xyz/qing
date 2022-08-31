pipeline {
    agent any
    stages {
        stage('阶段-1 拉取代码') {
            steps {
                // Get some code from a GitHub repository
                git credentialsId: 'GITEE_ACCESS_USERNAME', url: 'https://gitee.com/stanChen/zhangli.git'
            }
        }
        stage('阶段-3 打包') {
            steps {
                script {
                    sh "mvn  -pl zhangli-common,zhangli-service-provider clean package dockerfile:build dockerfile:tag dockerfile:push -DskipTests=true"
                }
            }
        }
    }
}