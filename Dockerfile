FROM openjdk:17

ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-Dspring.profiles.active=${PROFILE}", "-Duser.timezone=Asia/Seoul", "-jar","/app.jar"]

EXPOSE 8080
