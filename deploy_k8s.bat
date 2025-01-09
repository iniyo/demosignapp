@echo off
setlocal enabledelayedexpansion

:: ==========================================================
::  K8s Environment Switcher, Deployer, Progress Checker
:: ==========================================================

:MENU
cls
echo =============================================
echo   K8s Environment Switcher, Deployer, Runner
echo =============================================
echo 1) Minikube
echo 2) Docker Desktop
echo(
choice /c 12 /m "Pick your environment (1-2):"
if errorlevel 2 goto DESKTOP
if errorlevel 1 goto MINIKUBE

:: ----------------------------------------------------------------------
:MINIKUBE
echo [INFO] Switching context to Minikube...
kubectl config use-context minikube

echo [INFO] Building Docker image iniyo/mytestauthserver:latest ...
docker build -t iniyo/mytestauthserver:latest .

echo [INFO] Applying K8s resources in k8s folder (Minikube)...
kubectl apply -f k8s

echo [INFO] Waiting for deployment to complete...
kubectl rollout status deployment/demosignapp --timeout=180s
if errorlevel 1 (
    echo [ERROR] Deployment failed or timed out.
    goto END
)
goto OPEN_SWAGGER

:: ----------------------------------------------------------------------
:DESKTOP
echo [INFO] Switching context to Docker Desktop...
kubectl config use-context docker-desktop

echo [INFO] Building Docker image iniyo/mytestauthserver:latest ...
docker build -t iniyo/mytestauthserver:latest .

echo [INFO] Checking for existing container navni-auth...
docker ps -a --filter "name=navni-auth" --format "{{.Names}}" | findstr /C:"navni-auth" >nul
if %errorlevel%==0 (
    echo [WARNING] Existing container found. Removing it...
    docker rm -f navni-auth
)

echo [INFO] Running container navni-auth with Swagger enabled...
docker run -d --name navni-auth -p 8080:8080 iniyo/mytestauthserver:latest

echo [INFO] Checking container status...
set /a tries=0
:WAIT_CONTAINER
docker logs navni-auth 2>nul | findstr /C:"Started" >nul
if %errorlevel%==0 (
    echo [INFO] Container is ready!
    goto OPEN_SWAGGER
)

:: Retry if not ready
set /a tries+=1
if !tries! gtr 36 (
    echo [ERROR] Timed out waiting for navni-auth to be ready.
    goto END
)
timeout /t 5 >nul
goto WAIT_CONTAINER

:: ----------------------------------------------------------------------
:OPEN_SWAGGER
echo [INFO] Deployment successful. Opening Swagger UI...
start http://localhost:8080/swagger-ui.html
goto END

:: ----------------------------------------------------------------------
:END
echo(
echo [INFO] Deployment script completed. Press any key to close...
pause
endlocal
exit /b
