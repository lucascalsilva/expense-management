FROM openjdk:12-jdk-alpine
RUN addgroup -S mobnova && adduser -S mobnova -G mobnova
USER mobnova:mobnova
ARG WEB_JAR_FILE=expense-management-web/target/*.jar
COPY ${WEB_JAR_FILE} expense-management-web.jar
ENTRYPOINT ["java","-jar","/expense-management-web.jar"]
EXPOSE 8080