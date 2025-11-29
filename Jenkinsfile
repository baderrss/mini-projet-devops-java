pipeline {
    agent any

    tools {
        maven 'M3'
        jdk 'JDK21'
    }

    environment {
        PROJECT_NAME = 'mini-projet-devops-java'
        SONAR_PROJECT_KEY = "${PROJECT_NAME}"
        SONAR_PROJECT_NAME = 'Mini Projet DevOps Java'
        TOMCAT_URL_BASE = 'http://localhost:8081/manager/text'
        TOMCAT_USER = 'admin'
        TOMCAT_PASS = 'admin123'
    }

    stages {
        stage('Checkout GitHub') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/baderrss/mini-projet-devops-java.git',
                    credentialsId: 'github-credentials'
                sh '''
                    echo "✅ Étape 1/6 - Code récupéré depuis GitHub"
                    echo "📁 Contenu du repository:"
                    ls -la
                '''
            }
        }

        stage('Build Maven') {
            steps {
                sh '''
                    echo "🔨 Étape 2/6 - Installation des dépendances et compilation"
                    mvn clean compile -U
                    echo "✅ Application compilée avec succès"
                '''
            }
        }

        stage('Tests Unitaires') {
            steps {
                sh '''
                    echo "🧪 Étape 3/6 - Exécution des tests unitaires JUnit"
                    mvn clean test
                    echo "✅ Tests exécutés avec succès"
                '''
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                    sh 'echo "📊 Rapports de tests générés"'
                }
            }
        }

        stage('SAST - SonarQube') {
            steps {
                withSonarQubeEnv('sonarqube') {
                    sh """
                        echo "🔍 Étape 4/6 - Analyse SonarQube en cours..."
                        mvn clean verify sonar:sonar \
                            -Dsonar.projectKey=${SONAR_PROJECT_KEY} \
                            -Dsonar.projectName='${SONAR_PROJECT_NAME}' \
                            -Dsonar.host.url=http://192.168.190.130:9000 \
                            -Dsonar.login=your-sonar-token
                    """
                }
            }
        }

        stage('Quality Gate') {
            steps {
                script {
                    echo '📊 Étape 5/6 - Vérification Quality Gate...'
                    timeout(time: 2, unit: 'MINUTES') {
                        def qualityGate = waitForQualityGate()
                        if (qualityGate.status != 'OK') {
                            error "❌ Quality Gate ÉCHOUÉ: ${qualityGate.status}"
                        }
                        echo "✅ Quality Gate: ${qualityGate.status}"
                    }
                }
            }
        }

        stage('Deploy Tomcat') {
            steps {
                sh """
                    echo "🚀 Étape 6/6 - Déploiement sur Apache Tomcat"
                    echo "📦 Création du package WAR..."
                    mvn clean package -DskipTests

                    WAR_FILE="target/${PROJECT_NAME}.war"

                    echo "🌐 Déploiement sur Tomcat..."
                    curl -v -u ${TOMCAT_USER}:${TOMCAT_PASS} \
                         -T ${WAR_FILE} \
                         "${TOMCAT_URL_BASE}/deploy?path=/${PROJECT_NAME}&update=true"

                    echo "✅ Application déployée avec succès"

                    echo "🔍 Vérification du déploiement..."
                    curl -s -u ${TOMCAT_USER}:${TOMCAT_PASS} \
                         "${TOMCAT_URL_BASE}/list" | grep ${PROJECT_NAME} || echo "⚠️ Application non trouvée dans la liste"
                """
            }
        }
    }

    post {
        always {
            echo "📊 === RAPPORT FINAL DU PIPELINE ==="
            echo "🕒 Date: \$(date)"
            echo "🔧 Outils utilisés: JDK21, Maven, SonarQube, Tomcat10"
            echo "🌐 SonarQube Dashboard: http://192.168.190.130:9000/dashboard?id=${SONAR_PROJECT_KEY}"
            echo "🚀 Application déployée: http://192.168.190.130:8081/${PROJECT_NAME}/"
        }
        success {
            echo "🎉 === PIPELINE RÉUSSI ==="
            echo "✅ Toutes les étapes terminées avec succès!"
        }
        failure {
            echo "❌ === PIPELINE EN ÉCHEC ==="
            echo "🔍 Consultez les logs pour diagnostiquer le problème"
        }
    }
}