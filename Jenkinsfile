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
                    echo " Étape 1/6 - Code récupéré depuis GitHub"
                    echo " Contenu du repository:"
                    ls -la
                '''
            }
        }

        stage('Build Maven') {
            steps {
                sh '''
                    echo " Étape 2/6 - Installation des dépendances et compilation"
                    mvn clean compile -U
                    echo " Application compilée avec succès"
                '''
            }
        }

        stage('Tests Unitaires') {
            steps {
                sh '''
                    echo " Étape 3/6 - Exécution des tests unitaires JUnit"
                    mvn clean test
                    echo " Tests exécutés avec succès"
                '''
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                    sh 'echo " Rapports de tests générés"'
                }
            }
        }

        stage('SAST - SonarQube') {
            steps {
                withSonarQubeEnv('sonarqube') {
                    sh """
                        echo " Étape 4/6 - Analyse SonarQube en cours..."
                        mvn clean verify sonar:sonar \
                            -Dsonar.projectKey=${SONAR_PROJECT_KEY} \
                            -Dsonar.projectName='${SONAR_PROJECT_NAME}' \
                            -Dsonar.java.binaries=target/classes \
                            -Dsonar.sources=src/main/java \
                            -Dsonar.tests=src/test/java \
                            -Dsonar.sourceEncoding=UTF-8 \
                            -Dsonar.host.url=http://192.168.190.130:9000
                    """
                }
            }
        }

        stage('Quality Gate') {
            steps {
                script {
                    echo ' Étape 5/6 - Vérification Quality Gate...'
                    timeout(time: 5, unit: 'MINUTES') {
                        waitForQualityGate abortPipeline: true
                    }
                    echo " Quality Gate passé avec succès"
                }
            }
        }

        stage('Deploy Tomcat') {
            steps {
                sh """
                    echo " Étape 6/6 - Déploiement sur Apache Tomcat"

                    echo " Création du package WAR..."
                    mvn clean package -DskipTests

                    WAR_FILE="target/${PROJECT_NAME}-1.0-SNAPSHOT.war"

                    echo " Vérification du fichier WAR:"
                    ls -la target/*.war

                    echo " Arrêt de l'application existante..."
                    curl -s -u ${TOMCAT_USER}:${TOMCAT_PASS} \
                         "${TOMCAT_URL_BASE}/undeploy?path=/${PROJECT_NAME}" || echo "ℹ Aucune application à désinstaller"

                    sleep 5

                    echo " Déploiement de la nouvelle version..."
                    curl -v -u ${TOMCAT_USER}:${TOMCAT_PASS} \
                         -T "\${WAR_FILE}" \
                         "${TOMCAT_URL_BASE}/deploy?path=/${PROJECT_NAME}&update=true"

                    if [ \$? -eq 0 ]; then
                        echo " Application déployée avec succès"

                        echo " Attente du démarrage..."
                        sleep 15

                        echo " Vérification du déploiement:"
                        curl -s -u ${TOMCAT_USER}:${TOMCAT_PASS} \
                             "${TOMCAT_URL_BASE}/list" | grep "${PROJECT_NAME}" && echo "Application trouvée"

                        echo " Test d'accès à l'application:"
                        curl -f "http://localhost:8081/${PROJECT_NAME}/" && echo " Application accessible" || echo " Application non accessible"
                    else
                        echo " Échec du déploiement"
                        exit 1
                    fi
                """
            }
        }
    }

    post {
        always {
            echo " === RAPPORT FINAL DU PIPELINE ==="
            echo " Date: \$(date)"
            echo " Outils utilisés: JDK21, Maven, SonarQube, Tomcat10"
            echo " SonarQube Dashboard: http://192.168.190.130:9000/dashboard?id=${SONAR_PROJECT_KEY}"
            echo " Application déployée: http://192.168.190.130:8081/${PROJECT_NAME}/"
        }
        success {
            echo " === PIPELINE RÉUSSI ==="
            echo " Toutes les étapes terminées avec succès!"
        }
        failure {
            echo " === PIPELINE EN ÉCHEC ==="
            echo " Consultez les logs pour diagnostiquer le problème"
        }
    }
}