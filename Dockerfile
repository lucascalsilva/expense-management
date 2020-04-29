FROM openjdk:11
RUN addgroup mobnova && adduser mobnova --ingroup mobnova
USER mobnova:mobnova
ARG WEB_JAR_FILE=expense-management-web/target/*.jar
COPY ${WEB_JAR_FILE} expense-management-web.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/expense-management-web.jar"]