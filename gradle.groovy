def call()
{	
stage('Build Gradle')
 {            
            echo 'Building: Workspace -> [${env.WORKSPACE}]'                
            sh 'chmod -R 777 $WORKSPACE'
            sh './gradlew build'
  }
        stage('Sonar')
        {
                echo 'Sonar...'
                withSonarQubeEnv('Sonita') {
                    echo "$WORKSPACE" 
                    sh 'chmod -R 755 $WORKSPACE'
                    sh './gradlew sonarqube -Dsonar.projectKey=RemoteGradle --stacktrace --scan'
                }
        }

        stage('Run & Test')
        {
                //No es necesario hacer Kill del proceso
                echo 'Running Jar...'
                sh './gradlew bootRun&'
                sleep(60)
                sh 'curl -X GET http://localhost:8081/rest/mscovid/test?msg=testing'
        }

        stage('uploadNexus')
        {
                echo 'Uploading to Nexus...'
                //slackSend color: "warning", message: "Uploading to Nexus..."
                //sh './mvnw clean install -e'
                nexusPublisher nexusInstanceId: 'nexus', nexusRepositoryId: 'devops-usach-nexus', packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: '', filePath: './build/libs/DevOpsUsach2020-0.0.1.jar']], mavenCoordinate: [artifactId: 'DevOpsUsach2020', groupId: 'com.devopsusach2020', packaging: 'jar', version: '0.0.1']]]	
	}
}

return this;