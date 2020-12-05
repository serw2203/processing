```bash
cd ../centos8-oracle-jre8/

oc import-image centos:centos8 --confirm

oc new-build --strategy docker --binary --docker-image centos:centos8 --name centos8-oracle-jre

oc start-build centos8-oracle-jre --from-dir . --follow
```
