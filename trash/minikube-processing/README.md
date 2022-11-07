```bash
start minikube mount minikube-processing:/processing

mvn clean package -DskipTests=true && \
cp assembly/target/processing.jar minikube-processing/ && \
ls -la minikube-processing/processing.jar && 
\minikube ssh -- "docker build -t processing /processing" && \
kubectl scale deployment/processing --replicas=0 && \
kubectl scale deployment/processing --replicas=5 && 
kubectl get po -w

kubectl create -f minikube-processing/Deployment.yaml

kubectl expose deployment processing --type=NodePort --port=8080 --target-port=8080 --name=processing-service

kubectl expose deployment processing --type=NodePort --name=processing-service

kubectl scale deployment/processing --replicas=4

*kubectl run processing --image=processing:latest --image-pull-policy=Never

*cat <<EOF | kubectl create -f -
    ...
*EOF

curl --cookie "user-name=Sergey"  http://$(minikube ip):30701/env

kubectl delete deployment -l=app=processing

curl -s http://$(minikube ip):31840/agreement/registration | jq

kubectl apply -f ~/Desktop/processing/trash/minikube-processing/Deployment.yml

kubectl label namespace default istio-injection=enabled

kubectl port-forward svc/kiali 20001:20001 -n istio-system

https://istio.io/latest/docs/setup/additional-setup/sidecar-injection/

runmqsc <<< "define qlocal (DEV.QUEUE.4)"
```
```info

apiVersion: v1
kind: Service
metadata:
    name: processing-service
    namespace: trash
    labels:
        app: processing
spec:
    type: NodePort
    selector:
        app: processing
    ports:
        - port: 8080
          name: http
          targetPort: 8080
          protocol: TCP
```

172.23.80.1 boot.com

cd /C/Users/ariadna/Desktop/JAVA/processing/trash/ssl-test

openssl pkcs12 -export -out server.p12 -in server-crt.pem -inkey server-key.pem  -name boot

openssl pkcs12 -export -nokeys -in ca-crt.pem -out truststore.p12

keytool -import -file ca-crt.pem -keystore truststore.jks

curl -vvv https://boot.com/info1 --cert client1-crt.pem --key client1-key.pem --cacert ca-crt.pem

curl -sv https://boot.com/info1 --cert //opt//ps//tls.crt --key //opt//ps//tls.key --cacert //opt//ps//ca.crt

curl -sv https://boot.com/info1 --cert /opt/ps/tls.crt --key /opt/ps/tls.key --cacert /opt/ps/ca.crt

kcc exec `kubectl get po -l app=processing -n default -o name` -c processing -n default -- \
curl -sv https://boot.com/info1 \
--cert //opt//ps//tls.crt \
--key //opt//ps/tls.key \
--cacert //opt//ps//ca.crt

kcc logs -f `kubectl get po -l istio=egressgateway -n istio-system -o name` -n istio-system
kcc logs -f `kubectl get po -l app=processing -n default -o name` -n default -c istio-proxy

curl -sv http://boot.com:443/info1

kcc exec `kubectl get po -l app=processing -n default -o name` -c processing -n default -- curl -sv http://boot.com/info1 
