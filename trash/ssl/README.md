mkdir ssl
cd ssl

cat << EOF > req2.cnf
..
EOF

[req]
distinguished_name = req_distinguished_name
x509_extensions = v3_req
prompt = no
[req_distinguished_name]
C = RU
ST = RUSSIA
L = Korolev
O = Home
CN = 192.168.10.10:5000
[v3_req]
keyUsage = keyEncipherment, dataEncipherment
extendedKeyUsage = serverAuth
subjectAltName = @alt_names
[alt_names]
DNS.1 = IP:192.168.10.10
DNS.2 = 192.168.10.10:5000


openssl req -x509 -nodes -days 3650 -newkey rsa:2048 -keyout domain.key -out domain.crt -config req2.cnf -extensions 'v3_req'
openssl x509 -in domain.crt -noout -text

```
--создаёт корневой ключ
openssl genrsa -out rootCA.key 2048

--создаёт корневой сертификат
openssl req -x509 -new -key rootCA.key -days 10000 -out rootCA.crt

-- Генерируем ключ
openssl genrsa -out client.key 2048

-- запрос на сертификат
openssl req -new -key client.key -out client.csr

--подписать запрос на сертификат нашим корневым сертификатом
openssl x509 -req -in client.csr -CA rootCA.crt -CAkey rootCA.key -CAcreateserial -out client.crt -days 5000
```

cd ..
chmod 400 ssl/*

docker run -d \
  --restart=always \
  --name registry \
  -v "$(pwd)"/ssl:/certs \
  -e REGISTRY_HTTP_ADDR=0.0.0.0:443 \
  -e REGISTRY_HTTP_TLS_CERTIFICATE=/certs/domain.crt \
  -e REGISTRY_HTTP_TLS_KEY=/certs/domain.key \
  -p 443:443 \
  -p 5000:5000 \
  registry:2
  
mkdir /etc/docker/cert.d/192.168.10.10
cp ssl/domain.crt /etc/docker/cert.d/192.168.10.10
systemctl restart docker 

openssl s_client -showcerts -connect 192.168.10.10:443 

```NOT WORK
kubectl run nginx --image=192.168.10.10/nginx --overrides='{ "apiVersion": "v1", "spec": { "imagePullSecrets": [{"name": "registry-ca"}] } }'

kubectl apply -f - <<EOF
apiVersion: v1
kind: Pod
metadata:
  name: nginx
spec:
  containers:
    - name: nginx
      image: 192.168.10.10/nginx
      imagePullPolicy: Always
  imagePullSecrets: 
    - name: registry-ca
EOF
```

!!!
cp domain.crt C:\Users\ariadna\.minikube\certs\domain.pem

```bash
keytool -import -alias docker -file /c/Workspace/hv-centos8/ssl/domain.crt -keystore cacerts
```