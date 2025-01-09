@echo off
REM Docker Compose로 Redis 클러스터 컨테이너 실행
docker-compose up -d

REM Redis 노드가 생성되기를 기다림
timeout /t 10 > nul
echo Waiting for Redis nodes to initialize...

REM Redis 클러스터 네트워크가 있는지 확인하고 없으면 생성
docker network inspect redis-net > nul 2>&1
if errorlevel 1 (
    echo Creating Docker network for Redis nodes...
    docker network create redis-net
)

REM Redis 노드를 네트워크에 연결
echo Connecting Redis nodes to redis-net...
for %%A in (redis-node1 redis-node2 redis-node3) do (
    docker network connect redis-net %%A > nul 2>&1
)

REM Redis 노드의 IP 가져오기
for /f "tokens=*" %%A in ('docker inspect -f "{{(index .NetworkSettings.Networks \"redis-net\").IPAddress}}" redis-node1') do set NODE1_IP=%%A
for /f "tokens=*" %%A in ('docker inspect -f "{{(index .NetworkSettings.Networks \"redis-net\").IPAddress}}" redis-node2') do set NODE2_IP=%%A
for /f "tokens=*" %%A in ('docker inspect -f "{{(index .NetworkSettings.Networks \"redis-net\").IPAddress}}" redis-node3') do set NODE3_IP=%%A

REM IP 검증
if "%NODE1_IP%"=="" (
    echo Failed to retrieve IP addresses for redis-node1. Exiting...
    pause
    exit /b 1
)
if "%NODE2_IP%"=="" (
    echo Failed to retrieve IP addresses for redis-node2. Exiting...
    pause
    exit /b 1
)
if "%NODE3_IP%"=="" (
    echo Failed to retrieve IP addresses for redis-node3. Exiting...
    pause
    exit /b 1
)

echo Node 1 IP: %NODE1_IP%
echo Node 2 IP: %NODE2_IP%
echo Node 3 IP: %NODE3_IP%

REM Redis 클러스터 초기화
echo Resetting Redis nodes...
for %%A in (redis-node1 redis-node2 redis-node3) do (
    docker exec -it %%A redis-cli -p 6379 cluster reset
)

REM Redis 클러스터 생성
echo Creating Redis cluster...
docker exec -it redis-node1 redis-cli --cluster create %NODE1_IP%:6379 %NODE2_IP%:6379 %NODE3_IP%:6379 --cluster-replicas 0 --cluster-yes

REM 클러스터 상태가 ok로 변경될 때까지 대기
echo Waiting for cluster state to become OK...
:check_cluster
for /f "tokens=2 delims=:" %%A in ('docker exec -it redis-node1 redis-cli -p 6379 cluster info ^| findstr "cluster_state"') do set CLUSTER_STATE=%%A
if "%CLUSTER_STATE%"=="ok" (
    echo Cluster state is OK.
) else (
    timeout /t 5 > nul
    goto :check_cluster
)

REM 클러스터 상태 확인
echo Checking cluster status...
docker exec -it redis-node1 redis-cli -p 6379 cluster info

REM 클러스터 테스트
echo Testing Redis cluster...
docker exec -it redis-node1 redis-cli -c -p 6379 set test_key "Hello, Redis!"
docker exec -it redis-node1 redis-cli -c -p 6379 get test_key

echo Redis cluster setup complete.
pause
