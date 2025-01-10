@echo off
setlocal enabledelayedexpansion

:: ==========================================================
::  Kubernetes Resource Cleanup and Redeployment Script
:: ==========================================================

:MENU
cls
echo =============================================
echo   Kubernetes Resource Cleanup and Redeploy
echo =============================================
echo 1) Cleanup Resources
echo 2) Redeploy Resources
echo 3) Full Cleanup and Redeploy
echo(
choice /c 123 /m "Select an option:"
if errorlevel 3 goto FULL_CLEAN_DEPLOY
if errorlevel 2 goto REDEPLOY
if errorlevel 1 goto CLEANUP

:: ----------------------------------------------------------------------
:CLEANUP
echo [INFO] Deleting all Kubernetes resources in the current context...
kubectl delete all --all
if errorlevel 1 (
    echo [ERROR] Failed to delete resources. Check your configuration.
    goto END
)
echo [INFO] All resources deleted successfully.
goto END

:: ----------------------------------------------------------------------
:REDEPLOY
echo [INFO] Applying Kubernetes resources from the 'k8s/' folder...
kubectl apply -f k8s/
if errorlevel 1 (
    echo [ERROR] Redeployment failed. Check your configuration files.
    goto END
)
echo [INFO] Redeployment completed successfully.
goto END

:: ----------------------------------------------------------------------
:FULL_CLEAN_DEPLOY
echo [INFO] Performing full cleanup and redeployment...
kubectl delete all --all
if errorlevel 1 (
    echo [ERROR] Failed to delete resources during full cleanup.
    goto END
)
echo [INFO] All resources deleted successfully.

echo [INFO] Applying Kubernetes resources from the 'k8s/' folder...
kubectl apply -f k8s/
if errorlevel 1 (
    echo [ERROR] Redeployment failed during full cleanup and redeploy.
    goto END
)
echo [INFO] Full cleanup and redeployment completed successfully.
goto END

:: ----------------------------------------------------------------------
:END
echo(
echo [INFO] Script execution completed. Press any key to close...
pause
endlocal
exit /b
