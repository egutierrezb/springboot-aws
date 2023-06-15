FROM amazoncorretto:17-alpine-jdk
MAINTAINER alexgutierrez
COPY target/room-0.0.1-SNAPSHOT.jar room-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar", "/room-0.0.1-SNAPSHOT.jar"]
