FROM eclipse-temurin:17-centos7

EXPOSE 8080

COPY target/processing.jar processing.jar

ENTRYPOINT ["java" \
,"--add-opens", "java.base/java.time=ALL-UNNAMED" , "--add-opens", "java.base/java.util=ALL-UNNAMED" ,"--add-opens", "java.base/sun.util.calendar=ALL-UNNAMED" ,"--add-opens", "java.base/sun.util.locale=ALL-UNNAMED" ,"-jar", "processing.jar"]
