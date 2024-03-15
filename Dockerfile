FROM amazoncorretto:21

ENV SERVER_MODE=server_mode
ENV MYSQL_IP=mysql_ip
ENV MYSQL_PORT=mysql_port
ENV MYSQL_USERNAME=mysql_username
ENV MYSQL_PASSWORD=mysql_password
ENV USER_ACCESS_TOKEN_SECRET=user_access_token_secret
ENV USER_ACCESS_TOKEN_EXPIRE=user_access_token_expire
ENV USER_REFRESH_TOKEN_SECRET=user_refresh_token_secret
ENV USER_REFRESH_TOKEN_EXPIRE=user_refresh_token_expire
ENV EMPLOYEE_ACCESS_TOKEN_SECRET=employee_access_token_secret
ENV EMPLOYEE_ACCESS_TOKEN_EXPIRE=employee_access_token_expire
ENV EMPLOYEE_REFRESH_TOKEN_SECRET=employee_refresh_token_secret
ENV EMPLOYEE_REFRESH_TOKEN_EXPIRE=employee_refresh_token_expire

EXPOSE 8080

ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} /usr/local/bin/app.jar

ENTRYPOINT ["java", "-Dspring.profiles.active=${SERVER_MODE}", "-jar", "/usr/local/bin/app.jar"]
