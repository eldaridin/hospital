FROM eclipse-temurin:21-jre-alpine
LABEL authors="ELDARIDIN"
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8089
ENTRYPOINT ["java", "-jar", "app.jar"]