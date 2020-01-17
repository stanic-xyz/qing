pipeline {
    agent any
    stages {
        // stage('编译') { 
        //     steps {
        //         sh "mvn -B -DskipTests clean package"
        //     }
        // }
        stage('打包镜像,并推送到制品库') {
            steps {
                sh "chmod +x ./deliver.sh"
                sh "./delever.sh ."
              }
        }
    }
}