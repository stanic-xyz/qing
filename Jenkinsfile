pipeline {
  agent {
    docker {
      image 'maven:3-alpine'
      args '-v /root/.m2:/root/.m2'
      reuseNode true
    }
  }
  stages {
    stage('打包') {
      steps {
        script {
          if (isUnix() == true) {
            echo '这里linux系统'
            sh 'mvn clean package -DskipTests=true'
          } else {
            echo '这是windows系统'
            bat 'mvn clean package -DskipTests=true'
          }
        }
      }
    }
    stage('收集构建物') {
      steps {
        echo '收集构建物'
        archiveArtifacts(artifacts: '**/target/*.jar', onlyIfSuccessful: true, defaultExcludes: true)
      }
    }
    stage('构建成功通知') {
      steps {
        echo 'Build successfully !'
      }
    }
  }
}