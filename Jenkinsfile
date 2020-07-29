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
        sh 'mvn compile -DskipTests=true -Ddockerfile.skip'
      }
    }
    stage('打包') {
      steps {
        sh 'mvn  -DskipTests=true package'
      }
    }
  }
}