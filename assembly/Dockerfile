FROM adoptopenjdk/openjdk11:jdk-11.0.9.1_1

EXPOSE 8080

COPY target/processing.jar processing.jar

ENTRYPOINT ["java","-jar", "processing.jar"]
