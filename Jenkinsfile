pipeline {
    agent {
        label 'maven-agent'
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
            }
        }
        stage('编译打包 Domain') {
            steps {
                sh '''echo 开始编译'''
                sh '''mvn clean package -pl qing-domain -f pom.xml -DskipTests=true'''
            }
        }
    }
}
