    pipeline {
   agent {
        node {
            label 'Slave03'
        }
    }
    
    stages {
        stage('Install') {
             steps {
                sh 'mvn clean install -DskipTests'
                script {
                    version = sh(returnStdout: true, script: 'mvn help:evaluate -Dexpression=project.version | grep -e "^[^[]" ')
                 }
                 script {
                    version2 = sh(returnStdout: true, script: 'mvn help:evaluate -Dexpression=project.version | grep -e "^[^[]" | sed "s/-SNAPSHOT//g"')
                 }
                  echo version2.trim()
            }
        }

        stage('Sonar scan') {
            steps {
                withSonarQubeEnv('New Sonar Endava') {
                    sh 'mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.2:sonar'
                }
            }
        }

        stage('Upload artifact') {
           steps {
               nexusArtifactUploader artifacts: [[artifactId: 'add-project-internship-2018' + ${BUILD_NUMBER}, classifier: '', file: 'target/add-project-internship-2018-' + version.trim() + '.war', type: 'war']], 
               credentialsId: '9d977555-9613-4485-8c0c-a25b72a316e3', 
               groupId: 'com.endava', 
               nexusUrl: 'nexus.endava.net', 
               nexusVersion: 'nexus3', 
               protocol: 'https', 
               repository: 'Intens_2018_firs', 
               version: version2.trim()
           }
        }
        
        }     
}