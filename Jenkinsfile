pipeline {
    agent {
        docker {
            image 'maven:3.9.5-eclipse-temurin-17-alpine'
            args '-v /root/.m2:/root/.m2'
        }
    }
    stages {
        stage('编译 Domain') {
            when { branch comparator: 'EQUALS', pattern: 'main' }
            steps {
                sh '''echo 开始编译'''
                sh '''mvn clean install -pl qing-codegen-plugin/qing-codegen-apt -am -f pom.xml'''
                sh '''mvn clean package -pl qing-domain -am -f pom.xml'''
            }
        }
        stage('单元测试') {
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