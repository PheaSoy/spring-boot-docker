# Spring Boot Docker and Docker Swarm
## Building docker image
### Building image from traditional
Adding this to Dockerfile
```bash
FROM adoptopenjdk/openjdk11:ubi
EXPOSE 8080
ARG JAR_FILE=target/spring-boot-docker-swarm-0.0.1.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","-jar","/app.jar"]

```
```bash
docker build . -t boot-greeting:0.0.1
````
## Running the docker
### Simple start application
```bash
$ docker run -p 8080:8080 -t boot-greeting:0.0.1
```
### Parsing env variable to spring boot properties source
In our application.properties we had configuration.
```properties
greeting.message = Macbook Pro M1 Chip®
```
So now let parse this configuration from docker env.
```bash
$ docker run -p 8080:8080 -e "greeting.message=docker running env"  -t boot-greeting:0.0.1
```