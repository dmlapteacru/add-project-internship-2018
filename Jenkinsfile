pipeline {
    agent any
    stages {
        stage("Install") {
             steps {
                sh 'mvn clean install -DskipTests'
            }
        }
        stage("Upload artifact")
            steps {
                nexusArtifactUploader (
                    nexusVersion: 'nexus3',
                    protocol: 'http',
                    nexusUrl: 'nexus.endava.net',
                    groupId: 'com.endava',
                    version: '0.0.1',
                    repository: 'Intens_2018_firs',
                    credentialsId: 'CredentialsId',
                    artifacts: [
                        [artifactId: 'add-project-internship-2018',
                        classifier: '',
                        file: 'add-project-internship-2018-' + version + '.war',
                        type: 'war']
                    ]
                )
                
            }
    }
}