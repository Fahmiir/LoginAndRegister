pipeline {
    agent any

    environment {
        DB_URL = 'jdbc:oracle:thin:@host.docker.internal:1521/XEPDB1'
        DB_USERNAME = 'system'
        DB_PASSWORD = 'password'
    }

    stages {

        stage('Build') {
            steps {
                bat 'mvn clean package -DskipTests'
            }
        }

        stage('Test') {
            steps {
                bat 'mvn test'
            }
        }

        stage('Deploy') {
            steps {
                bat 'docker stop login-app'
                bat 'docker rm -f login-app'
                bat 'docker compose up -d --build'
            }
        }

    }
}