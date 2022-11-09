istioctl proxy-config route `kubectl get po -l app=processing -o name` \
--name 80 \
-o json | jq --color-output | cat -n