# Utilise une image Maven pour builder l'application
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
COPY schemas ./schemas
RUN mvn clean package -DskipTests

# Utilise une image JRE légère pour exécuter l'application
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
COPY schemas ./schemas
COPY data ./data
COPY uploads ./uploads
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"] 