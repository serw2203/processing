FROM image-registry.openshift-image-registry.svc:5000/trash/centos8-oracle-jre8

EXPOSE 8080

COPY processing.jar processing.jar

ENTRYPOINT ["java","-jar", "processing.jar"]