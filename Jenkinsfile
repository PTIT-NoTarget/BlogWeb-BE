pipeline {
    agent {label "demo"}
    stages{
        stage("Check old image") {
            steps {
                sh 'docker rm -f demo || echo "this container does not exist" '
                sh 'docker image rm -f demo || echo "this image dose not exist" '
            }
        }
        stage('clean') {
            steps {
                sh 'chmod +x mvnw'
                sh "./mvnw -ntp clean -P-webapp"
            }
        }
        stage('packaging') {
            steps {
                sh "./mvnw -ntp verify -P-webapp -Pdev -Dmaven.test.skip -Dcheckstyle.skip"
            }
        }
        stage('Build and Run') {
            steps {
                sh 'docker compose up -d --build'
            }
        }
    }
}
