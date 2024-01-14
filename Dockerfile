FROM openjdk:21-jdk

COPY ./build/libs/sba-server.jar /sba-server.jar

ENV TZ=Europe/Moscow

ENTRYPOINT ["java", "-jar", "/sba-server.jar"]