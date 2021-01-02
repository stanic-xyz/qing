pipeline {
    agent {
        docker {
            image 'maven:3-alpine'
            args '-v /root/.m2:/root/.m2'
            reuseNode true
        }
    }
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
                    if (isUnix() == true) {
                        echo 'linux'
                        sh "mvn  -pl zhangli-service-provider clean package -DskipTests=true"
                    } else {
                        echo 'windows'
                        bat "mvn  -pl zhangli-service-provider clean package -DskipTests=true"
                    }
                }
            }
        }
        stage('阶段-4 收集构建物') {
            steps {
                echo '收集构建物'
                archiveArtifacts (artifacts: '**/target/*.jar', onlyIfSuccessful: true, defaultExcludes: true)
            }
        }
        stage('阶段-5 打包鏡像') {
            steps {
                sh "mvn -pl zhangli-service-provider dockerfile:build dockerfile:tag dockerfile:push"
            }
        }

        stage('阶段-6 打標籤') {
            steps {
                sh "mvn -pl zhangli-service-provider dockerfile:tag"
            }
        }

        stage('阶段-7 推送到製品庫') {
            steps {
                sh "mvn -pl zhangli-service-provider dockerfile:push"
            }
        }
    }
}