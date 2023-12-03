pipeline {
    agent any
    stages {
        stage('编译') {
            steps {
                sh '''echo 开始编译'''
                sh '''mvn clean package -pl qing-domain -f pom.xml'''
            }
        }
        stage('编译') {
            steps {
                sh "mvn test  -pl qing-domain -f pom.xml"
            }
            post {
                always {
                    // 收集测试报告
                    junit 'qing-domain/target/surefire-reports/*.xml'
                }
            }
        }
    }
}