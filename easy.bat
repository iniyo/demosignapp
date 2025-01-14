kubectl delete -f k8s/deployment.yaml

./gradlew build

docker build -t redis-demo:latest .

kubectl apply -f k8s/deployment.yaml