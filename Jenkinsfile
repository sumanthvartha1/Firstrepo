pipeline {
    agent any
    
    tools {
        maven 'Maven'
    }
    
    options {
        timestamps()
        timeout(time: 30, unit: 'MINUTES')
        buildDiscarder(logRotator(numToKeepStr: '10'))
    }
    
    environment {
        APP_NAME = 'payment-service'
        SONAR_PROJECT_KEY = 'payment-service'
    }
    
    stages {
        stage('1. Checkout') {
            steps {
                checkout scm
                echo "Building ${APP_NAME} #${env.BUILD_NUMBER}"
                echo "Running on: ${env.NODE_NAME}"
            }
        }
        
        stage('2. Build') {
            steps {
                echo 'Compiling source code...'
                sh 'mvn clean compile -DskipTests'
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
        
        stage('4. Parallel Security Scans') {
            parallel {
                stage('4a. SonarQube SAST') {
                    steps {
                        echo 'Running SonarQube code analysis...'
                        withSonarQubeEnv('SonarQube') {
                            sh 'mvn sonar:sonar'
                        }
                    }
                }
                
                stage('4b. OWASP Dependency Check SCA') {
                    steps {
                        echo 'Scanning dependencies for vulnerabilities...'
                        sh 'mvn dependency-check:check'
                    }
                }
            }
        }
        
        stage('5. Quality Gate') {
            steps {
                echo 'Waiting for SonarQube Quality Gate result...'
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
                echo 'Quality Gate: PASSED'
            }
        }
        
        stage('6. Package & Upload to Nexus') {
            steps {
                echo 'Creating WAR artifact...'
                sh 'mvn package -DskipTests'
                
                echo 'Uploading to Nexus Repository...'
                sh 'mvn deploy -DskipTests'
            }
        }
        
        stage('7. Deploy') {
            steps {
                echo "Deploying ${APP_NAME} version 1.0.0"
                echo 'Deployment complete'
            }
        }
    }
    
    post {
        success {
            echo "✅ Pipeline SUCCESS: ${APP_NAME} built and deployed"
        }
        failure {
            echo "❌ Pipeline FAILED: Check console output"
        }
        always {
            echo "Build complete. Check SonarQube dashboard for quality report."
        }
    }
}
