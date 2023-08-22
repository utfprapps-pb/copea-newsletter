pipeline {
    agent any

    environment {
        // DB
        POSTGRESQL_CRED = credentials('postgres-id')
        DB_JDBC_USER = "${POSTGRESQL_CRED_USR}"
        DB_JDBC_PASSWORD = "${POSTGRESQL_CRED_PSW}"
        DB_JDBC_DATABASE = "copea-newsletter"       
        DB_JDBC_URL="jdbc:postgresql://postgresql:5432/copea-newsletter"
        // API
        PORT_API="8620"
        TZ_API="America/Sao_Paulo"
        
        // WEB
        API_URL="https://copea-newsletter-api.app.pb.utfpr.edu.br/api"
        PORT_WEB="80"
        TZ_WEB="America/Sao_Paulo"
    }

    stages {       
        stage('API - Build') {    
            steps {
                dir("api-java") {
                    sh 'docker compose -f docker-compose.yaml up -d --build'
                }
            }
        }
        stage('Client Web - Build') {    
            steps {
                dir("web-angular") {
                    sh 'docker compose -f docker-compose.yaml up -d --build'
                }
            }
        }
    }

    post {
        always {
            echo 'Build finished.'
        }
        success {
            echo 'Build succeeded.'
        }
        failure {
            echo 'Build failed.'
        }
    }
}
