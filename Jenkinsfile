pipeline {
    agent any
    stages {
        stage('构建Domain服务') {
            steps {
                sh '''echo hello CODING
mvn clean install -pl qing-domain -am -f pom.xml'''
            }
        }
    }
}