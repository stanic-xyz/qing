pipeline {
  agent any
  stages {
    stage('打包') {
      steps {
        bat 'package.cmd'
      }
    }

    stage('收集构建物') {
      steps {
        archiveArtifacts(artifacts: '**/target/*.jar', allowEmptyArchive: true, defaultExcludes: true, onlyIfSuccessful: true, fingerprint: true)
      }
    }

  }
}