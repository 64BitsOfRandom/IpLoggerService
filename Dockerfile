FROM openjdk:17-alpine
RUN addgroup -S demo && adduser -S demo -G demo
USER demo
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} ip-logger-service.jar
ENTRYPOINT ["java", "-jar", "ip-logger-service.jar"]