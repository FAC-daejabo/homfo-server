FROM amazoncorretto:21

ENV SERVER_MODE=server_mode
ENV MYSQL_IP=mysql_ip
ENV MYSQL_PORT=mysql_port
ENV MYSQL_USERNAME=mysql_username
ENV MYSQL_PASSWORD=mysql_password
ENV ACCESS_TOKEN_SECRET=access_token_secret
ENV ACCESS_TOKEN_EXPIRE=access_token_expire
ENV REFRESH_TOKEN_SECRET=refresh_token_secret
ENV REFRESH_TOKEN_EXPIRE=refresh_token_expire

EXPOSE 8080

ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} /usr/local/bin/app.jar

ENTRYPOINT ["java", "-Dspring.profiles.active=${SERVER_MODE}", "-jar", "/usr/local/bin/app.jar"]
