pipeline {
  agent any
  stages {
    stage('打包') {
      steps {
        script {
          if(isUnix() == true) {
            echo '这里linux系统'
            bat 'mvn clean package -Ddockerfile.skip=true -DskipTests=true'
          }else {
            echo '这是windows系统'
            sh 'mvn clean package -Ddockerfile.skip=true -DskipTests=true'
          }
        }
      }
    }
  }
}