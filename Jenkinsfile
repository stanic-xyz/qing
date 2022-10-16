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
        stage('阶段-2 打包') {
            steps {
                script {
                    if (isUnix() == true) {
                        sh "cd ${WORKSPACE}"
                        sh "mvn clean -pl 'zhangli-service-provider' package"
                    } else {
                        bat "cd ${WORKSPACE}"
                        bat "mvn clean package"
                    }
                }
            }
        }
        stage('阶段-4 收集构建物') {
            steps {
                archiveArtifacts(artifacts: '**/target/*.jar', onlyIfSuccessful: true, defaultExcludes: true)
            }
        }
    }
}
