# Étape 1 : Utiliser une image de base avec JDK
FROM eclipse-temurin:17-jdk-jammy

# Étape 2 : Définir le répertoire de travail dans le conteneur
WORKDIR /app

# Étape 3 : Copier le fichier JAR généré par Maven
COPY target/Scolarite-*.jar app.jar

# Étape 4 : Exposer le port sur lequel l'application tourne
EXPOSE 8080

# Étape 5 : Commande pour exécuter l'application
ENTRYPOINT ["java", "-jar", "app.jar"]