/*
 * Copyright (c) 2023  YunLong Chen
 * Project Qing is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 *
 */

pipeline {
    agent {
        docker {
            reuseNode true
            registryUrl 'https://coding-public-docker.pkg.coding.net'
            image 'public/docker/openjdk:17'
            args '-v /root/.gradle/:/root/.gradle/ -v /root/.m2/:/root/.m2/'
        }

    }
    stages {
        stage('阶段-1 拉取代码') {
            steps {
                checkout([
                        $class           : "GitSCM",
                        branches         : [[name: env.GIT_BUILD_REF]],
                        userRemoteConfigs: [[url: env.GIT_REPO_URL, credentialsId: env.CREDENTIALS_ID]]])
            }
        }

        stage('阶段-2 打包') {
            steps {
                sh 'mvn clean install --file qing-bom/pom.xml -DskipTests=true -B'
                sh 'mvn clean install --file qing-commons/pom.xml -DskipTests=true  -B'
                sh 'mvn clean install --file qing-common-starters/pom.xml -DskipTests=true  -B'
                sh 'mvn clean install --file qing-codegen-plugin/pom.xml -DskipTests=true  -B'
                sh 'mvn package --file qing-service-anime/pom.xml -DskipTests=true  -B'
            }
        }

        stage('构建 Docker 镜像') {
            steps {
                useCustomStepPlugin(key: 'coding-public:magic-version', version: 'latest')
                script {
                    readProperties(file: env.CI_ENV_FILE).each { key, value -> env[key] = value }
                }
                script {
                    ARTIFACT_VERSION = "${env.MAGIC_VERSION}"
                    // 注意：创建项目时链接标识不要使用下划线，而是连字符，比如 My Project 的标识应为 my-project
                    // 请修改 build/my-api 为你的制品库名称和镜像名称
                    CODING_DOCKER_IMAGE_NAME = "${env.PROJECT_NAME.toLowerCase()}/qing/qing-service-anime"
                    docker.withRegistry("https://${env.CCI_CURRENT_TEAM}-docker.pkg.coding.net", "${env.CODING_ARTIFACTS_CREDENTIALS_ID}") {
                        docker.build("${CODING_DOCKER_IMAGE_NAME}:${ARTIFACT_VERSION}", '-f Dockerfile ./qing-service-anime').push()
                    }
                }
            }
        }
    }
}
