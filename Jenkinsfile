def responseStatus = ''

pipeline {
    agent any
        
    stages {
        stage('INFO'){
            steps{
                echo 'Info...'
                //slackSend color: "warning", message: "INFO: Prueba Taller 3 - Modulo 4 Branch: ${GIT_BRANCH}"
            }
            post {
                success {
                    echo 'INFO Success'
                    //slackSend color: "good", message: "Info Success. commit ${GIT_COMMIT}"
                }
                failure {
                    echo 'INFO Failed'
                    //slackSend color: "danger", message: "Info Failed."
                }
            }
        }
        stage('SONARQube'){
            steps{
                echo 'Sonar...'
                withSonarQubeEnv('Sonita') {
                    echo "$WORKSPACE" 
                    sh 'chmod -R 755 $WORKSPACE'
                    sh './gradlew sonarqube -Dsonar.projectKey=RemoteGradle --stacktrace --scan'
                }
            }
            post {
                success {
                    echo 'SONAR Success' 
                    //
                }
                failure {
                    echo 'SONAR Failed'
                    //slackSend color: "danger", message: "Sonar Failed."
                }
            }
        }
        stage('BUILD'){
            steps{
                echo 'Building...'
                sh './gradlew build'
            }
            post {
                success {
                    echo 'Build Success'
                    //slackSend color: "good", message: "Build Success"
                }
                failure {
                    echo 'Build Failed'
                    //slackSend color: "danger", message: "Build Failed"
                }
            }
        }
        stage('RUN'){
            steps{
                echo 'Running...'
                sh './gradlew bootRun&'
                //sh 'curl -X GET http://localhost:8081/rest/mscovid/test?msg=testing'
                sh 'curl -I GET http://localhost:8081/rest/mscovid/test?msg=testing > response.txt'
                //${responseStatus} = Corregir sh(script: 'cat response.txt | grep HTTP/1.1 | cut -d " " -f2', returnStdout: true).trim()
            }
            post {
                success {
                    echo 'Running Success'
                    //slackSend color: "good", message: "Build Success"
                }
                failure {
                    echo 'Running Failed'
                    //slackSend color: "danger", message: "Build Failed"
                }
            }
        }
         stage('NEXUS'){
            steps{
                echo 'Uploading to Nexus...'
                //slackSend color: "warning", message: "Uploading to Nexus..."
                //sh './mvnw clean install -e'
                nexusPublisher nexusInstanceId: 'nexus', nexusRepositoryId: 'devops-usach-nexus', packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: '', filePath: './build/libs/DevOpsUsach2020-0.0.1.jar']], mavenCoordinate: [artifactId: 'DevOpsUsach2020', groupId: 'com.devopsusach2020', packaging: 'jar', version: '0.0.1']]]
            }
            post {
                success {
                    echo 'Upload Success'
                    //slackSend color: "good", message: "Nexus Upload Success"
                }
                failure {
                    echo 'Upload Failed'
                    //slackSend color: "danger", message: "Nexus Upload Failed"
                }
            }
         }
    }
}