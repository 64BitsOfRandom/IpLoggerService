FROM openjdk:17-alpine
RUN addgroup -S demo && adduser -S demo -G demo
USER demo
#provide actual IP
#ENV ENV_HOST_IP='51.250.102.83'
#ENV ENV_HOST_PORT='8080'
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} ip-logger-service.jar
ENTRYPOINT ["java", "-jar", "ip-logger-service.jar"]