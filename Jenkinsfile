pipeline {
  agent {
    docker {
      image 'maven'
      args '-v ~/.m2:/root/.m2'
    }

  }
  stages {
    stage('打包') {
      steps {
        sh 'mvn clean package -Ddockerfile.skip=true -DskipTests=true'
      }
    }

    stage('收集构建物') {
      steps {
        archiveArtifacts(artifacts: '**/target/*.jar', allowEmptyArchive: true, fingerprint: true, onlyIfSuccessful: true)
      }
    }

  }
}