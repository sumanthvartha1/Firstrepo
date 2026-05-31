pipeline {
    agent any
    
    tools {
        maven 'Maven'
    }
    
    options {
        timestamps()
        timeout(time: 30, unit: 'MINUTES')
    }
    
    environment {
        APP_NAME = 'payment-service'
    }
    
    stages {
        stage('1. Checkout') {
            steps {
                checkout scm
                echo "Building ${APP_NAME} #${env.BUILD_NUMBER}"
            }
        }
        
        stage('2. Build') {
            steps {
                echo 'Compiling source code...'
                sh 'mvn clean compile'
            }
        }
        
        stage('3. Unit Tests') {
            steps {
                echo 'Running unit tests...'
                sh 'mvn test'
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
                }
            }
        }
        
        stage('4. Package') {
            steps {
                echo 'Creating WAR file...'
                sh 'mvn package -DskipTests'
                echo 'Build complete!'
            }
        }
    }
    
    post {
        success {
            echo "✅ Pipeline SUCCESS"
        }
        failure {
            echo "❌ Pipeline FAILED"
        }
    }
}

