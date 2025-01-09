@echo off
REM Kubernetes Secret 생성 스크립트

echo Creating Kubernetes secret: kakao-secret...
kubectl create secret generic kakao-secret ^
  --from-literal=client-id=ba4ad9422c578c47f67e3d3b16c84b53 ^
  --from-literal=client-secret=6T2gEREyLHs5SaEvZHdzBYD4mgInFaLA

if %errorlevel% neq 0 (
  echo Failed to create secret. Please check your Kubernetes configuration.
  exit /b %errorlevel%
) else (
  echo Secret "kakao-secret" has been successfully created.
)
pause
