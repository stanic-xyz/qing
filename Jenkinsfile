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
    stage('检出代码') {
      steps {
        checkout(scm: [
          $class: 'GitSCM',
          branches: [[name: env.GIT_BUILD_REF]],
          userRemoteConfigs: [[
            url: env.GIT_REPO_URL,
            credentialsId: env.CREDENTIALS_ID
          ]]], changelog: true, poll: false)
        }
      }
      stage('编译') {
        steps {
          sh 'mvn compile -DskipTests=true'
        }
      }
    }
  }