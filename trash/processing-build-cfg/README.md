```bash
cd ../processing-build-cfg/

oc new-build --strategy docker --binary --docker-image image-registry.openshift-image-registry.svc:5000/trash/centos8-oracle-jre8 --name processing-build-cfg

oc start-build processing-build-cfg --from-dir . --follow
```

```bash
oc import-image openjdk11:latest --from=adoptopenjdk/openjdk11 --confirm

oc new-build --strategy docker --binary --docker-image adoptopenjdk/openjdk11 --name processing

oc start-build processing --from-dir . --follow
```