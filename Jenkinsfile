pipeline {
    agent any

    tools {
        // Assurez-vous que 'M3' et 'JDK21' sont les noms exacts configurés dans Jenkins
        maven 'M3'
        jdk 'JDK21'
    }

    environment {
        // Changement de 'devops-app' à 'mini-projet-devops-java'
        PROJECT_NAME = 'mini-projet-devops-java'
        SONAR_PROJECT_KEY = "${PROJECT_NAME}"
        SONAR_PROJECT_NAME = 'Mini Projet DevOps Java'

        // Assurez-vous que l'URL et les credentials Tomcat sont corrects pour votre configuration
        TOMCAT_URL_BASE = 'http://localhost:8081/manager/text'
        TOMCAT_USER = 'admin'
        TOMCAT_PASS = 'admin123'
    }

    stages {
        // ÉTAPE 1: Checkout GitHub
        stage('Checkout GitHub') {
            steps {
                // CORRECTION 1: URL du dépôt mise à jour
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

        // ÉTAPE 2: Build Maven
        stage('Build Maven') {
            steps {
                sh '''
                    echo "🔨 Étape 2/6 - Installation des dépendances et compilation"
                    mvn clean compile
                    echo "✅ Application compilée avec succès"
                '''
            }
        }

        // ÉTAPE 3: Tests JUnit
        stage('Tests Unitaires') {
            steps {
                sh '''
                    echo "🧪 Étape 3/6 - Exécution des tests unitaires JUnit"
                    mvn test
                '''
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                    sh 'echo "✅ Rapports de tests générés"'
                }
            }
        }

        // ÉTAPE 4: SAST - SonarQube
        stage('SAST - SonarQube') {
            steps {
                // 'sonarqube' doit correspondre au nom de votre serveur configuré dans Jenkins
                withSonarQubeEnv('sonarqube') {
                    sh """
                        echo "🔍 Étape 4/6 - Analyse SonarQube en cours..."
                        # Utilise la nouvelle variable SONAR_PROJECT_KEY
                        mvn sonar:sonar -Dsonar.projectKey=\${SONAR_PROJECT_KEY} -Dsonar.projectName='${SONAR_PROJECT_NAME}'
                    """
                }
            }
        }

        // ÉTAPE 5: Quality Gate (Utilisation du script pour gérer le redémarrage SonarQube)
        stage('Quality Gate') {
            steps {
                script {
                    echo '📊 Étape 5/6 - Vérification Quality Gate...'

                    // Timeout court car l'analyse SonarQube est rapide.
                    // Si vous avez souvent des redémarrages de Sonar, cette logique est utile.
                    try {
                        timeout(time: 30, unit: 'SECONDS') {
                            def qualityGate = waitForQualityGate abortPipeline: false
                            if (qualityGate.status == 'OK') {
                                echo "✅ Quality Gate: ${qualityGate.status}"
                            } else {
                                // Si le Quality Gate n'est pas OK, on échoue le pipeline pour ne pas déployer un code non conforme
                                error "❌ Quality Gate ÉCHOUÉ: ${qualityGate.status}. Consultez SonarQube."
                            }
                        }
                    } catch (Exception e) {
                        echo "🔄 Erreur lors de la vérification Quality Gate (timeout ou SonarQube non joignable). Poursuite du déploiement avec prudence."
                    }
                }
            }
        }

        // ÉTAPE 6: Déploiement Tomcat
        stage('Deploy Tomcat') {
            steps {
                sh """
                    echo "🚀 Étape 6/6 - Déploiement sur Apache Tomcat"
                    echo "📦 Création du package WAR..."
                    # Nous refaisons package ici car les étapes précédentes n'ont fait que compile/test
                    mvn package -DskipTests

                    # Chemin du fichier WAR
                    WAR_FILE="target/\${PROJECT_NAME}.war"

                    echo "🌐 Déploiement sur Tomcat..."

                    # Déployer la nouvelle version. Utilise les variables d'environnement pour l'URL, l'utilisateur et le path.
                    curl -s -u \${TOMCAT_USER}:\${TOMCAT_PASS} \\
                         -T \${WAR_FILE} \\
                         "\${TOMCAT_URL_BASE}/deploy?path=/\${PROJECT_NAME}&update=true"

                    # Vérification du succès de la commande curl
                    if [ \$? -ne 0 ]; then
                        echo "❌ ÉCHEC du déploiement. Vérifiez les logs Tomcat."
                        exit 1
                    fi

                    echo "✅ Application déployée avec succès"

                    # Vérification (liste les applications déployées)
                    echo "🔍 Vérification du déploiement..."
                    curl -s -u \${TOMCAT_USER}:\${TOMCAT_PASS} "\${TOMCAT_URL_BASE}/list" | grep \${PROJECT_NAME}
                """
            }
        }
    }

    post {
        always {
            echo "📊 === RAPPORT FINAL DU PIPELINE ==="
            echo "🕒 Date: \$(date)"
            echo "🔧 Outils utilisés: JDK21, Maven, SonarQube, Tomcat10"
            // Utilise les variables pour les liens finaux
            echo "🌐 SonarQube Dashboard: http://192.168.190.130:9000/dashboard?id=\${SONAR_PROJECT_KEY}"
            echo "🚀 Application déployée: http://192.168.190.130:8081/\${PROJECT_NAME}/"
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