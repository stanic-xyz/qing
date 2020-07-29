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
      agent {
        docker {
          image 'maven:3-alpine'
          reuseNode true
          args '-v /root/.m2:/root/.m2'
        }

      }
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
        agent {
          docker {
            image 'maven:3-alpine'
            reuseNode true
            args '-v /root/.m2:/root/.m2'
          }

        }
        steps {
          sh 'mvn compile -DskipTests=true'
        }
      }
      stage('部署') {
        agent {
          docker {
            image 'maven:3-alpine'
            reuseNode true
            args '-v /root/.m2:/root/.m2'
          }

        }
        steps {
          sh 'mvn package -DskipTests=true -Ddockerfile.skip'
        }
      }
    }
  }