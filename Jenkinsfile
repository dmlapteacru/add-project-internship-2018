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
                    url = 'http://othervpcdotnet-1019633969.us-east-1.elb.amazonaws.com:5001'
                    link_net = readFile 'src/main/resources/application.properties'
                    link_net = link_net.replaceFirst("/(bank.connection.endpoint=).+api\$/", "\$1${url}/api")
                    echo link_net
                    writeFile file: 'src/main/resources/application.properties', text: "$link_net"
                    sh 'cat src/main/resources/application.properties'
                }
                script {
                    version = sh(returnStdout: true, script: 'mvn help:evaluate -Dexpression=project.version | grep -e "^[^[]" | sed "s/-SNAPSHOT//g"')
                    version = version.trim()
                 }
                sh "mvn versions:set -DnewVersion=$version-$env.BUILD_NUMBER"
                sh 'mvn clean install -DskipTests'

                script {
                    version2 = sh(returnStdout: true, script: 'mvn help:evaluate -Dexpression=project.version | grep -e "^[^[]" | sed "s/-SNAPSHOT//g"')
                    version2 = version2.trim()
                 }
            }
        }

        // stage('Jacoco Code Coverage') {
        //     steps {
        //         jacoco execPattern: '**/target/**.exec'
        //     }
        // }

        stage('Sonar scan') {
            steps {
                withSonarQubeEnv('New Sonar Endava') {
                    sh 'mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.2:sonar'
                }
            }
        }

        stage('Build dockerfile') {
            steps {
                //sh 'docker build -t internship_java_team1_spring_2018 .'  
                sh 'docker build -t final-1 .'
            }
        }

        stage('Push image on ECR') {
            steps {
                withCredentials([usernamePassword(credentialsId: '1829e512-98c0-4c98-9293-253d5a7a3704', passwordVariable: 'aws_secret_access_key', usernameVariable: 'aws_access_key_id')]) {
                        sh "aws configure set aws_access_key_id $aws_access_key_id "
                        sh "aws configure set aws_secret_access_key $aws_secret_access_key "
                        sh "aws configure set default.region us-east-1"

                        sh 'aws ecr get-login --no-include-email | bash'
                        
                       // sh "docker tag internship_java_team1_spring_2018 543633097370.dkr.ecr.us-east-1.amazonaws.com/internship_java_team1_spring_2018:$version"
                       // sh "docker push 543633097370.dkr.ecr.us-east-1.amazonaws.com/internship_java_team1_spring_2018:$version"
                        sh "docker tag final-1:latest 543633097370.dkr.ecr.us-east-1.amazonaws.com/final-1:$version2"
                        sh "docker push 543633097370.dkr.ecr.us-east-1.amazonaws.com/final-1:$version2"
                        
            }
        }
    }

    stage('Update service'){
        steps {
          script {
             task_def = readFile 'final-1-task-definition.json'
             task_def = task_def.replace("/(543633097370.dkr.ecr.us-east-1.amazonaws.com/final-1:)*\"/", "\$1$version2")
             writeFile file: 'final-1-task-definition.json', text: "$task_def"
          }
           sh 'aws ecs register-task-definition --requires-compatibilities FARGATE --network-mode awsvpc --cpu 2048 --memory 4096 --execution-role-arn ecsTaskExecutionRole --cli-input-json file://final-1-task-definition.json'
           sh 'aws ecs describe-task-definition --task-definition java1new > taskdef.json'
           script {
                rev_ver = sh(returnStdout: true, script:'aws ecs describe-task-definition --task-definition java1new | grep "revision" | cut -d: -f2 | cut -d, -f1 | tr -d " " ')
           }
           sh "aws ecs update-service --cluster internship2018march --service java1new-service --task-definition java1new:$rev_ver "
        }
    }
        
    }     
}
