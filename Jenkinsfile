pipeline {
   agent {
        node {
            label 'Slave06'
        }
    }
    
    stages {
        stage('Install') {
             steps {
                sh 'mvn clean install -DskipTests'
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

        stage('Build dockerfile') {
            steps {
                sh 'docker build -t internship_java_team1_spring_2018 .'  
            }
        }

        stage('Push image on ECR') {
            steps {
            withCredentials([usernamePassword(credentialsId: '1829e512-98c0-4c98-9293-253d5a7a3704', passwordVariable: 'aws_secret_access_key', usernameVariable: 'aws_access_key_id')]) {
                        sh "aws configure set aws_access_key_id $aws_access_key_id "
                        sh "aws configure set aws_secret_access_key $aws_secret_access_key "
                        sh "aws configure set default.region us-east-1"

                        sh 'aws ecr get-login --no-include-email | bash'
                        
                        sh 'docker tag internship_java_team1_spring_2018:latest 543633097370.dkr.ecr.us-east-1.amazonaws.com/internship_java_team1_spring_2018:latest'
                        sh 'docker push 543633097370.dkr.ecr.us-east-1.amazonaws.com/internship_java_team1_spring_2018:latest'

                        sh 'docker tag internship_java_team1_spring_2018 543633097370.dkr.ecr.us-east-1.amazonaws.com/internship_java_team1_spring_2018:v0.1.1'
                        sh 'docker push 543633097370.dkr.ecr.us-east-1.amazonaws.com/internship_java_team1_spring_2018:v0.1.0'
            }
        }
    }
        
    }     
}
