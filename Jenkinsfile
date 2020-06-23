pipeline {
  agent {
    docker {
      image 'maven:3-alpine'
      args '-v /root/.m2:/root/.m2'
      reuseNode true
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
    stage('测试') {
      post {
        always {
          junit 'target/surefire-reports/*.xml'

        }

      }
      steps {
        sh 'mvn test'
      }
    }
    stage('打包镜像') {
      steps {
        sh 'echo skiped!'
      }
    }
  }
}