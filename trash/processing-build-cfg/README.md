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

```bash
curl http://$(minikube ip)/info1 -H "host: processing.com"

kcc exec `kubectl get po -l app=processing -n default -o name` -c processing -n default -- curl -s http://boot.com/info1

kcc exec -it `kubectl get po -l app=processing -n default -o name` -c processing -n default -- bash
```

STACKOVERFLOW

https://stackoverflow.com/questions/59643479/istio-egress-mtls-for-external-services

kcc exec `kubectl get po -l istio=egressgateway -n istio-system -o name` -n istio-system -- cat //etc//istio

kubectl get secret processing-secret -n default -o yaml \
| sed s/"namespace: default"/"namespace: istio-system"/\
| kubectl apply -n istio-system -f -

kubectl get secret processing-server-secret -n default -o yaml \
| sed s/"namespace: default"/"namespace: istio-system"/\
| kubectl apply -n istio-system -f -

openssl pkcs7 -inform DER -outform PEM -in certificate.p7b -print_certs
