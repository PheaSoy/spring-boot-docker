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
```bash
curl http://localhost:8080/greeting 
```
Result
```text
Hello World from docker running env
```

## Container Image Registry
### Setup docker-registry
```bash
 docker run -d \
  -p 5000:5000 \
  --restart=always \
  --name registry \
  -v /Users/soyphea/Documents/data/docker-registry/:/var/lib/registry \
  registry:2
```
Please note this path `/Users/soyphea/Documents/data/docker-registry/` is binding to /var/lib/registry for the docker to prevent the lost of data
### Pushing to container registry
Tag the container image for pushing to local container registry
```bash
docker tag boot-greeting:0.0.1 localhost:5000/greet-boot
```
Pushing image to local registry
```bash
docker push localhost:5000/greet-boot
```
### Running the container from container image in registry
```bash
docker run -p 8080:8080 -e "greeting.message=docker running env"  -t localhost:5000/greet-boot
```
### Running in docker swarm 
Create service for docker swarm
```bash
 docker service create --name boot-greeting-swarm --publish 8080:8080 localhost:5000/greet-boot
```