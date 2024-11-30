pipeline {
    agent any

    environment {
        TIMESTAMP = "${new Date().format('yyyyMMddHHmmss')}"
        def imageName = "presentation-app:${env.TIMESTAMP}"
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
                    sh "cd gatling"
                    sh "./mvnw gatling:test"
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
