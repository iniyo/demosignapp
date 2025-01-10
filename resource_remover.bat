@echo off
chcp 65001 >nul
echo Kubernetes 및 Docker 리소스를 모두 삭제합니다.

:: Kubernetes 리소스 삭제
echo 모든 Kubernetes 리소스 삭제 중...
kubectl delete all --all --all-namespaces >nul 2>&1
kubectl delete namespaces $(kubectl get namespaces -o=jsonpath="{.items[*].metadata.name}") --ignore-not-found >nul 2>&1
kubectl delete pv,pvc --all --all-namespaces >nul 2>&1
kubectl delete configmap,secret --all --all-namespaces >nul 2>&1

:: Kubernetes 클러스터 초기화 (선택 사항)
echo Minikube 클러스터 정리 중...
minikube delete >nul 2>&1

:: Docker 컨테이너 중지 및 삭제
echo Docker의 모든 컨테이너 중지 및 삭제 중...
docker stop $(docker ps -aq) >nul 2>&1
docker rm $(docker ps -aq) >nul 2>&1

:: Docker 이미지 삭제
echo 모든 Docker 이미지를 삭제 중...
docker rmi $(docker images -aq) --force >nul 2>&1

:: Docker 네트워크 삭제
echo 모든 Docker 네트워크 삭제 중...
docker network rm $(docker network ls -q) >nul 2>&1

:: Docker 볼륨 삭제
echo 모든 Docker 볼륨 삭제 중...
docker volume rm $(docker volume ls -q) >nul 2>&1

:: Docker 시스템 정리
echo Docker 시스템 정리 중...
docker system prune -af --volumes >nul 2>&1

echo 모든 Kubernetes 및 Docker 리소스가 삭제되었습니다.
pause
