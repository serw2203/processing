FROM adoptopenjdk/openjdk11

EXPOSE 8080

COPY target/processing.jar processing.jar

ENTRYPOINT ["java","-jar", "processing.jar"]
