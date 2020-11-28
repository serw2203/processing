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

kubectl scale deployment/processing --replicas=4

*kubectl run processing --image=processing:latest --image-pull-policy=Never

*cat <<EOF | kubectl create -f -
    ...
*EOF

curl --cookie "user-name=Sergey"  http://$(minikube ip):30701/env
```info


