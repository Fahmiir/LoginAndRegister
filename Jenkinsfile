pipeline {
    agent any

    environment {
        DB_URL = 'jdbc:oracle:thin:@localhost:1521/XEPDB1'
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

        stage('Docker Build') {
            steps {
                bat 'docker build -t login-register-app .'
            }
        }

        stage('Run Container') {
            steps {
                bat 'docker run -d -p 8080:8080 -e DB_URL="%DB_URL%" -e DB_USERNAME="%DB_USERNAME%" -e DB_PASSWORD="%DB_PASSWORD%" --name login-register-2 login-register-app'
            }
        }

    }
}