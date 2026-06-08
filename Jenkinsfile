pipeline {
    agent any

    environment {
        DB_URL = 'jdbc:oracle:thin:@localhost:1522/XEPDB1'
        DB_USERNAME = 'system'
        DB_PASSWORD = 'password'
    }

    stages {

        stage('Build') {
            steps {
                bat 'mvn clean package -DskipTests'
            }
        }

        stage('Deploy') {
            steps {
                bat 'docker rm -f login-app'
                bat 'docker compose up -d --build'
            }
        }

    }
}