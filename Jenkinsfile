pipeline {
  agent any
  stages {
    stage('打包') {
      steps {
        bat 'mvn clean package -Ddockerfile.skip=true -DskipTests=true'
      }
    }
  }
}