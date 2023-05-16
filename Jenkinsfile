pipeline {
    agent any

    environment {
    	// DB
        POSTGRES_USER="postgres"     
        POSTGRES_PASSWORD="admin"
        POSTGRES_DB="newsletter"
        TZ_DB="America/Sao_Paulo"
        
        // API
        DB_JDBC_URL="jdbc:postgresql://newsletter-db:5432/newsletter?ApplicationName=newsletter"
        DB_JDBC_USER="postgres"
        DB_JDBC_PASSWORD="admin"
        PORT_API="8080"
        TZ_API="America/Sao_Paulo"
        
        // WEB
        API_URL="http://localhost:8080/api"
        PORT_WEB="80"
        TZ_WEB="America/Sao_Paulo"
    }

    stages {   
        stage('Docker Compose UP') {
            steps {
                sh 'docker compose up -d --build'
            }
        }
    }
}
