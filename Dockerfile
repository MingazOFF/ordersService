FROM openjdk:21

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} OS_app.jar

ENTRYPOINT ["java", "-jar", "OS_app.jar"]

EXPOSE 8090
