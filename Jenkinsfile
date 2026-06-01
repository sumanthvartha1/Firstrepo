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
        
        stage('4. Parallel Security Scans') {
            parallel {
                stage('4a. SonarQube SAST') {
                    steps {
                        echo 'Running SonarQube code analysis...'
                        withSonarQubeEnv('SonarQube') {
                            sh 'mvn sonar:sonar'
                        }
                        echo 'SonarQube scan complete.'
                    }
                }
                
                stage('4b. OWASP Dependency Check') {
                    steps {
                        echo 'Scanning dependencies for vulnerabilities...'
                        sh 'mvn dependency-check:check'
                        echo 'OWASP scan complete.'
                    }
                    post {
                        always {
                            dependencyCheckPublisher pattern: 'target/dependency-check-report.xml'
                        }
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
                echo '✅ Quality Gate: PASSED'
            }
        }
        
        stage('6. Package') {
            steps {
                echo 'Creating WAR file...'
                sh 'mvn package -DskipTests'
                echo 'Artifact ready: target/payment-service-1.0.0.war'
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

