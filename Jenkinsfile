pipeline {
  agent {
    docker {
      image 'maven:3-alpine'
      args '-v /root/.m2:/root/.m2'
      reuseNode true
      registryUrl ''
    }

  }
  stages {
    stage('编译') {
      steps {
        sh 'mvn  -DskipTests=true compile'
      }
    }
    stage('打包') {
      steps {
        sh 'mvn  -DskipTests=true package'
      }
    }
    stage('推送') {
      steps {
        sh 'echo hello CODING'
      }
    }
  }
}