
first if u don't installed chcolatey(only window). this command use that powershell(manager mode)

Get-ExecutionPolicy
ECHO if Restricted, use that
Set-ExecutionPolicy AllSigned

ECHO and now
Set-ExecutionPolicy Bypass -Scope Process -Force; [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072; iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))

ECHO and use choco install helm
choco install kubernetes-helm

ECHO If u first helm repository created, you should be this command
helm repo add bitnami https://charts.bitnami.com/bitnami

----------------------------------------- set -----------------------------------------------

ECHO set custom values
helm install redis-cluster bitnami/redis -f helm/values.yaml
If Error: INSTALLATION FAILED: Kubernetes cluster unreachable: ? -> docker desktop kubernetes enabled

ECHO create pod redis client config
kubectl run --namespace default redis-client --restart='Never' --env REDIS_PASSWORD=redis-password --image docker.io/bitnami/redis:7.4.2-debian-12-r0 --command -- sleep infinity

ECHO connect pod redis client
kubectl exec --tty -i redis-client --namespace default -- bash

ECHO If you removed redis cluster when we use that command
helm uninstall redis-cluster

ECHO and you now distribution redis cluster
helm install redis-cluster bitnami/redis -f helm/values.yaml

----------------------------------------------- check -----------------------------------------------------

ECHO check cluster info
kubectl cluster-info

ECHO check config
kubectl config view

ECHO check redis password
kubectl get secret --namespace default redis-cluster -o jsonpath="{.data.redis-password}" | ForEach-Object { [System.Text.Encoding]::UTF8.GetString([System.Convert]::FromBase64String($_)) }

ECHO pods info
kubectl get pods -A

ECHO If U check the only Redis cluster. U use that command
kubectl get pods -l app.kubernetes.io/name=redis-cluster

ECHO U Check the cluster Info, if state is ok? use success -> cluster_state:ok
kubectl exec -it redis-cluster-0 -- redis-cli -c
CLUSTER INFO

--------------------------------- change and uninstall ---------------------------------

kubectl delete -f k8s/deployment.yaml
kubectl delete -f k8s/service.yaml
kubectl apply -f k8s/deployment.yaml
kubectl apply -f k8s/service.yaml
kubectl get pods -n default
kubectl get svc auth-app