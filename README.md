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
 $ docker run -d \
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
$ docker tag boot-greeting:0.0.1 localhost:5000/greet-boot
```
Pushing image to local registry
```bash
$ docker push localhost:5000/greet-boot
```
### Running the container from container image in registry
```bash
$ docker run -p 8080:8080 -e "greeting.message=docker running env"  -t localhost:5000/greet-boot
```

## Docker and Spring Boot environment variable
`-e SPRING_PROFILES_ACTIVE=local` this spring will use `local`as profile
```bash
docker run -e SPRING_PROFILES_ACTIVE=local -p 8080:8080 boot-greeting:0.0.3
```
### Environment from file
`--env-file application-docker.properties` this spring will use this file as variable environment
```bash
docker run --env-file application-docker.properties -p 8080:8080 boot-greeting:0.0.2
```
## Running in docker swarm 
Create service for docker swarm
```bash
$ docker service create --name boot-greeting-swarm --publish 8080:8080 localhost:5000/greet-boot
```
Scaling the service 
```bash
$ docker service scale boot-greeting-swarm=5
```
Listing the service

```bash
$ docker service ls
```
Result => 
```bash
ID             NAME                  MODE         REPLICAS   IMAGE                              PORTS
uxst1xujm7th   boot-greeting-swarm   replicated   5/5        localhost:5000/greet-boot:latest   *:8080->8080/tcp
```
List all the task for a service
```bash
$ docker service ps  boot-greeting-swarm
```
Result => 
```bash
ID             NAME                    IMAGE                              NODE             DESIRED STATE   CURRENT STATE                ERROR     PORTS
tec1dtu6ia95   boot-greeting-swarm.1   localhost:5000/greet-boot:latest   docker-desktop   Running         Running 4 minutes ago                  
s5o459t6fb6r   boot-greeting-swarm.2   localhost:5000/greet-boot:latest   docker-desktop   Running         Running 4 minutes ago                  
yqqamjto12f6   boot-greeting-swarm.3   localhost:5000/greet-boot:latest   docker-desktop   Running         Running about a minute ago             
mk226ba5ua6p   boot-greeting-swarm.4   localhost:5000/greet-boot:latest   docker-desktop   Running         Running about a minute ago             
08vga417upd7   boot-greeting-swarm.5   localhost:5000/greet-boot:latest   docker-desktop   Running         Running about a minute ago 
```

### Update the service
```bash
$ docker service update --image localhost:5000/greeting-boot:0.0.3  boot-greeting-swarmç
```

### Using delay update for zero downtime 
`--update-delay 10s` this will tell swarm to delay 10 seconds before it is going to update another task.
```bash
$ docker service update  --update-delay 10s --image localhost:5000/greeting-boot:0.0.3  boot-greeting-swarm
```

### Rolling back your swarm service
```bash
$ docker service rollback  boot-greeting-swarm
```
