kubectl create -f 'https://strimzi.io/install/latest?namespace=kafka' -n kafka

cat << EOF | kubectl apply -n kafka -f -
..
EOF


cat strimzi-cluster-operator-0.25.0.yaml | sed 's/namespace: .*/namespace: kafka/' | kc apply -f - -n kafka

curl -d '{"sDRs":[{"sDR" : [{"desc" : "Hello"}, {"desc" : "Hello2"}]}]}' -H "Content-Type: application/json" -X POST http://localhost:8080/test

curl -d '{"sDRs":["st1","st2"]}' -H "Content-Type: application/json" -X POST http://localhost:8080/test
