VAULT_UNSEAL_KEY=$(cat /C/Users/ariadna/Desktop/JAVA/processing/trash/egress/shell/keys.json | jq -r ".unseal_keys_b64[]")
VAULT_ROOT_KEY=$(cat /C/Users/ariadna/Desktop/JAVA/processing/trash/egress/shell/keys.json | jq -r ".root_token")
kubecolor exec vault-0 -- vault operator unseal $VAULT_UNSEAL_KEY
kubecolor exec vault-0 -- vault login $VAULT_ROOT_KEY