FROM maven:3.8.5-jdk-11

WORKDIR /mioProgetto

COPY target/mioProgetto-1.0-SNAPSHOT.jar .