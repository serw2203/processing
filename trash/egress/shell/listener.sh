istioctl proxy-config listener `kubectl get po -l app=processing -o name` \
-o json | jq --color-output | cat -n