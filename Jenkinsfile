pipeline {
  agent {
      docker {
          image 'maven:3-alpine'
          args '-v /root/.m2:/root/.m2'
      }
  }
  stages {
      stage('编译') {
        steps {
          sh 'mvn compile -DskipTests=true'
        }
      }
      stage('打包') {
        steps {
          sh 'mvn package -Ddockerfile.skip=true -DskipTests=true'
        }
      }
      stage('收集构建物') {
        steps {
          archiveArtifacts(artifacts: '**/target/*.jar', allowEmptyArchive: true, fingerprint: true, onlyIfSuccessful: true)
        }
      }
      stage('构建Docker镜像') {
        steps {
          sh 'mvn com.spotify:dockerfile-maven-plugin:1.4.4:build'
        }
      }
      stage('打标签') {
        steps {
          sh 'mvn com.spotify:dockerfile-maven-plugin:1.4.4:tag'
        }
      }
    }
  }