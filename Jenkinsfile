pipeline {
    agent {
        docker {
            image 'maven:3-alpine' 
            args '-v /root/.m2:/root/.m2' 
        }
    }
    environment {
        ENTERPRISE = "stanics"
        PROJECT = "zhangli"
        ARTIFACT = "zhangli"
        CODE_DEPOT = "zhangli"
        TAG_NAME = "0.0.1"

        ARTIFACT_BASE = "${ENTERPRISE}"
        ARTIFACT_IMAGE="${ARTIFACT_BASE}/${PROJECT}"
    }
    stages {
        stage('编译') { 
            steps {
                sh 'mvn -B -DskipTests clean package' 
            }
        }
        // stage('测试') { 
        //     steps {
        //         sh 'mvn test' 
        //     }
        //     post {
        //         always {
        //             junit 'target/surefire-reports/*.xml' 
        //         }
        //     }
        // }
        stage('打包镜像,并推送到制品库') {
            steps {
                sh "echo docker build -t ${ARTIFACT_IMAGE}:${TAG_NAME} ."
                sh "echo docker push ${ARTIFACT_IMAGE}:${TAG_NAME}"
                sh "echo docker tag ${ARTIFACT_IMAGE}:${TAG_NAME} ${ARTIFACT_IMAGE}:latest"
                sh "echo docker push ${ARTIFACT_IMAGE}:latest"
              }
        }
        // stage('推送到制品库') {
        //     steps {
        //         sh 'chmod +x ./jenkins/scripts/deliver.sh'
        //         sh './jenkins/scripts/deliver.sh'
        //     }
        // }
    }
}