def responseStatus = ''
def varScript

pipeline {
    agent any
    
    parameters 
    {        
        choice(name: "Tool", choices: ["maven", "gradle"], description: "Seleccione su herramienta de construcción")
        booleanParam(name: "upload", defaultValue: true, description: "¿Actualizar repositorio Nexus?")
    }     
    stages {
        stage('INFO'){
            steps{
                echo 'Info...'
               }
            post {
                success {
                    echo 'INFO Success'
                }
                failure {
                    echo 'INFO Failed'
                }
            }
        }     
        stage('Build')
        {
            steps
            {
                script
                {
                    if(params.Tool == 'gradle') 
                    {
                        echo 'Llamamos a gradle.groovy'
                        varScript = load 'gradle.groovy'                                            
                        varScript.call()   
                    }
                    else
                    {
                        echo 'Llamamos maven.groovy'
                        varScript = load 'maven.groovy'                        
                        varScript.call()   
                    }
                }                                 
            }
            post {
                success {
                    echo 'Build Success ' + params.Tool
                }
                failure {
                    echo 'Build Failed ' + params.Tool
                }
            }
        }        
    }
}