pipeline {
   agent {
        node {
            label 'Slave06'
        }
    }
    
    stages {
        stage('Install') {
             steps {
                script {
                    version = sh(returnStdout: true, script: 'mvn help:evaluate -Dexpression=project.version | grep -e "^[^[]" ')  
                    version = version.trim()
                 }
                 echo 'target/add-project-internship-2018-' + version.trim() + '.war'
                 script {
                    version2 = sh(returnStdout: true, script: 'mvn help:evaluate -Dexpression=project.version | grep -e "^[^[]" | sed "s/-SNAPSHOT//g"')
                 }

                //sh "mvn versions:set -DnewVersion=$version-$env.BUILD_NUMBER"
                sh 'mvn clean install -DskipTests'

                script {
                    version = sh(returnStdout: true, script: 'mvn help:evaluate -Dexpression=project.version | grep -e "^[^[]" ')
                 }
                 echo 'target/add-project-internship-2018-' + version.trim() + '.war'
                 script {
                    version2 = sh(returnStdout: true, script: 'mvn help:evaluate -Dexpression=project.version | grep -e "^[^[]" | sed "s/-SNAPSHOT//g"')
                 }
                
            }
        }

        // stage('Jacoco Code Coverage') {
        //     steps {
        //         jacoco execPattern: '**/target/**.exec'
        //     }
        // }

        // stage('Sonar scan') {
        //     steps {
        //         withSonarQubeEnv('New Sonar Endava') {
        //             sh 'mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.2:sonar'
        //         }
        //     }
        // }

        // stage('Upload artifact') {
        //    steps {
        //        nexusArtifactUploader artifacts: [[artifactId: 'add-project-internship-2018', classifier: '', file: 'target/add-project-internship-2018-' + version.trim() + '.war', type: 'war']], 
        //        credentialsId: '9d977555-9613-4485-8c0c-a25b72a316e3', 
        //        groupId: 'com.endava', 
        //        nexusUrl: 'nexus.endava.net', 
        //        nexusVersion: 'nexus3', 
        //        protocol: 'https', 
        //        repository: 'Intens_2018_firs', 
        //        version: version2.trim()
        //    }
        // }

        stage('Build dockerfile') {
            steps {
                sh 'docker build -t java_team1 .'  
            }
        }

        stage('Push image on ECR') {
            steps {
            withCredentials([usernamePassword(credentialsId: '1829e512-98c0-4c98-9293-253d5a7a3704', passwordVariable: 'aws_secret_access_key', usernameVariable: 'aws_access_key_id')]) {
                        sh "aws configure set aws_access_key_id $aws_access_key_id "
                        sh "aws configure set aws_secret_access_key $aws_secret_access_key "
                        sh "aws configure set default.region us-east-1"

                        sh 'aws ecr get-login --no-include-email | bash'
                        
                        sh 'docker tag java_team1 543633097370.dkr.ecr.us-east-1.amazonaws.com/java_team1:v0.1'
                        sh 'docker push 543633097370.dkr.ecr.us-east-1.amazonaws.com/java_team1:v0.1'
            }
        }
    }
        
    }     
}
