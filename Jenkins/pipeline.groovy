pipeline {
    agent any

    environment {
        TIMESTAMP = "${new Date().format('yyyyMMddHHmmss')}"
        def imageName = "presentation-app:${env.TIMESTAMP}"
    }

    stages {
        stage('Clean Workspace') {
            steps {
                sh 'docker stop $(docker ps -a -q --filter \'ancestor=presentation-app\')'
                sh 'docker rm \$(docker ps -a -q --filter \'ancestor=presentation-app\')'
                cleanWs()
            }
        }

        stage('Checkout Code') {
            steps {
                git branch: 'master', url: 'https://github.com/abderrezakBoukazouha/gatling-poc'
            }
        }

        stage('Build and Test Docker Image') {
            steps {
                // Build and test Docker image
                script {
                    sh "docker build -t ${imageName} ."
                }
            }
        }

        stage('launch containerized application') {
            steps {
                // Build and test Docker image
                script {
                    sh "docker run -p 8010:8010 -d ${imageName}"
                }
            }
        }

        stage('launch gatling tests') {
            steps {
                script {
                    sh "ls -a"
                    sh "./mvnw -f ./gatling gatling:test"
                }
            }
        }
    }

    post {
        always {
            // Archive the Gatling results
            archiveArtifacts artifacts: '**/*.html', fingerprint: true

            // Optionally use the Gatling plugin if installed
            gatlingArchive()

            // delete unused containers
            sh 'docker stop $(docker ps -a -q --filter \'ancestor=presentation-app\')'
            sh 'docker rm \$(docker ps -a -q --filter \'ancestor=presentation-app\')'
        }
        success {
            echo 'Pipeline executed successfully.  '
        }
        failure {
            echo 'Pipeline failed.'
        }
    }
}
