kubecolor exec `kubectl get po -l app=processing -n default -o name` -c processing -n default -- \
curl -sv http://boot.com/info1