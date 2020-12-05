```bash
cd ../processing-build-cfg/

oc new-build --strategy docker --binary --docker-image image-registry.openshift-image-registry.svc:5000/trash/centos8-oracle-jre8 --name processing-build-cfg

oc start-build processing-build-cfg --from-dir . --follow
```
