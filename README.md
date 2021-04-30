# Spring Boot Docker and Docker Swarm
## Building 
### Building image from traditional 
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
# spring-boot-docker
