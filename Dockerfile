# Utiliser une image de base Java
FROM openjdk:17-jdk

# Définir le répertoire de travail
WORKDIR /app

# Copier les fichiers JAR dans le conteneur
COPY ./target/flutter-0.0.1-SNAPSHOT.jar app.jar

# Exposer le port 8080 pour votre application
EXPOSE 3000

# Définir la commande d'exécution
CMD ["java", "-jar", "app.jar"]