FROM eclipse-temurin:21-jdk-alpine
VOLUME \tmp
COPY appointmentbookingsystem/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
EXPOSE 8080