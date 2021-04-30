FROM adoptopenjdk/openjdk11:ubi
EXPOSE 8080
ARG JAR_FILE=target/spring-boot-docker-swarm-0.0.1.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
