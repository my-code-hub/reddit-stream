# Reddit activity API

This project is an implementation of a web service that reads reddit submission stream from
[http://stream.pushshift.io/](http://stream.pushshift.io/) and exposes an API that can be
consumed by a front-end application.

## Running locally

java 11 and MongoDB required to run the service locally. Use command: 

```bash 
./gradlew bootRun
```

This repository also includes .war file:

```bash 
/binary/homework-1.0.0.war
```

which can be deployed on the Apache Tomcat server.


## API

Available API specification can be viewed in Swagger UI - [http://localhost:8888/swagger-ui.html#/](http://localhost:8888/swagger-ui.html#/)
