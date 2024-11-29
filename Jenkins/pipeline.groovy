pipeline {
    agent any

    environment {
        TIMESTAMP = "${new Date().format('yyyyMMddHHmmss')}"
    }

    stages {
        stage('Clean Workspace') {
            steps {
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
                    def imageName = "presentation-app:${env.TIMESTAMP}"
                    sh "docker build -t ${imageName} ."
                }
            }
        }
    }

    post {
        always {
            echo 'Pipeline execution complete.'
        }
        success {
            echo 'Pipeline executed successfully.'
        }
        failure {
            echo 'Pipeline failed.'
        }
    }
}
