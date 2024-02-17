FROM amazoncorretto:21

EXPOSE 8080

ARG JAR_FILE=build/libs/*.jar

RUN mkdir -p /usr/local/bin

COPY ${JAR_FILE} /usr/local/bin/app.jar

ENTRYPOINT ["java", "-Dspring.profiles.active=${SERVER_MODE}", "-jar", "/usr/local/bin/app.jar"]
