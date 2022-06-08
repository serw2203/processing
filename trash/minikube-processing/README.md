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


