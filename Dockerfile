# Utiliser une image Java de base
FROM openjdk:17-jdk

# Définir le répertoire de travail
WORKDIR /app

# Copier le fichier JAR de l'application
COPY ./target/*.jar app.jar

# Définir la commande pour exécuter l'application
CMD ["java", "-jar", "app.jar"]