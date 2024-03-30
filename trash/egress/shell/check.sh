kcc apply -f - << E
apiVersion: v1
kind: Pod
metadata:
  name: webapp
  namespace: default
  annotations:
    sidecar.istio.io/inject: "false"
    vault.hashicorp.com/agent-inject: "true"
    vault.hashicorp.com/role: "webapp"
    vault.hashicorp.com/agent-inject-secret-config.txt: "kv/dev/apps/service01"
spec:
  containers:
  - image: nginx:latest
    name: nginx
  serviceAccountName: vault-auth
E

jwt_token=$(kubectl exec vault-client -- cat //var//run//secrets//kubernetes.io//serviceaccount//token)

curl -s --request POST --data '{"jwt": "'$jwt_token'", "role": "webapp"}' \
http://minikube:32000/v1/auth/kubernetes/login | jq --color-output | cat -n

curl -H "X-Vault-Token: s.SFHjxkONnZ12Zp6vWHzCcVsd" -H "X-Vault-Namespace: vault" \
-X GET http://minikube:32000/v1/demo-app/data/user01?version=1 -s  | jq --color-output | cat -n

curl -H "X-Vault-Token: s.p2lHqwdnmco1tsLCA9goT1A6" -H "X-Vault-Namespace: vault" \
-X GET http://minikube:32000/v1/kv/data/dev/apps/service01?version=1 -s  | jq --color-output | cat -n

Инжектор агента хранилища - это контроллер веб-хуков с мутацией Kubernetes.

