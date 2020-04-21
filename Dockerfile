FROM circleci/openjdk:11-browsers-legacy
ARG WEB_JAR_FILE=expense-management-web/target/*.jar
COPY ${WEB_JAR_FILE} expense-management-web.jar
ENTRYPOINT ["java","-jar","/expense-management-web.jar"]
EXPOSE 8080