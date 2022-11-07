openssl genrsa -out ca-key.pem 4096

openssl req -new -x509 -days 9999 -config ca.cnf -key ca-key.pem -out ca-crt.pem

openssl genrsa -out server-key.pem 4096

openssl req -new -config server.cnf -key server-key.pem -out server-csr.pem

openssl x509 -req -extfile server.cnf -days 999 -passin "pass:password" -in server-csr.pem -CA ca-crt.pem -CAkey ca-key.pem -CAcreateserial -out server-crt.pem

openssl genrsa -out client1-key.pem 4096

openssl req -new -config client1.cnf -key client1-key.pem -out client1-csr.pem

openssl x509 -req -extfile client1.cnf -days 999 -passin "pass:password" -in client1-csr.pem -CA ca-crt.pem -CAkey ca-key.pem -CAcreateserial -out client1-crt.pem

openssl verify -CAfile ca-crt.pem server-crt.pem

openssl verify -CAfile ca-crt.pem client1-crt.pem

keytool -import -file ca-crt.pem -keystore truststore.jks

