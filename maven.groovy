def call()
{	
  stage('Build Maven')
  {            
      echo '(Pre) Building'                
      sh 'chmod -R 777 $WORKSPACE'            
      sh './mvnw clean compile -e'
      echo '(Post) Building'                
  }
  
  stage('Test Maven')
  {
      echo "(Pre) Testing"
      sh './mvnw test -e'
      echo "(Post) Testing"
  }
  
  stage('Package Maven')
  {
      echo "(Pre) Packaging Maven"
      sh './mvnw package -e'
      echo "(Post) Packaging Maven"
  }

  stage('Sonar Maven')
  {
      echo '(Pre) Sonar Maven'
      withSonarQubeEnv('Sonita') 
      { // If you have configured more than one global server connection, you can specify its name
        sh './mvnw clean package sonar:sonar'
      }
      echo '(Post) Sonar Maven'
  }
    
  stage('Run & Test Maven')
  {
      //No es necesario hacer Kill del proceso
      echo '(Pre)Run & Test Maven'        
      //slackSend color: "warning", message: "Running Jar..."      
      sh './mvnw spring-boot:run &'
      sleep(210)
      sh 'curl -X GET http://localhost:8081/rest/mscovid/test?msg=testing'
      echo '(Post)Run & Test Maven'
  }

stage('uploadNexus')
{
  echo '(Pre) uploadNexus'
  //slackSend color: "warning", message: "Uploading to Nexus..."
  sh './mvnw clean install -e'
  nexusPublisher nexusInstanceId: 'nexus', nexusRepositoryId: 'devops-usach-nexus', packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: '', filePath: './build/DevOpsUsach2020-0.0.1.jar']], mavenCoordinate: [artifactId: 'DevOpsUsach2020', groupId: 'com.devopsusach2020', packaging: 'jar', version: '0.0.1']]]	
  echo '(Post) uploadNexus'  
}
}

return this;