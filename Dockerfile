# Étape 1 : Compiler le projet avec Maven
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

# Copier le projet
COPY pom.xml .
COPY src ./src

# Compiler l’application (et générer le .jar dans /app/target)
RUN mvn clean package -DskipTests

# Étape 2 : Image minimale pour exécuter le JAR
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copier le fichier JAR depuis le container builder
COPY --from=builder /app/target/*.jar app.jar

# Exposer le port
EXPOSE 8080

# Commande pour lancer l'application
ENTRYPOINT ["java", "-jar", "app.jar"]
