pipeline {
  agent any
  stages {
    stage('打包') {
      steps {
        sh 'mvn clean package -Ddockerfile.skip=true -DskipTests=true'
      }
    }
  }
}