# Stop Android Studio if running
Get-Process studio64 -ErrorAction SilentlyContinue | Stop-Process -Force

# Clean Gradle caches
Remove-Item -Path "$env:USERPROFILE\.gradle\caches\*" -Recurse -Force -ErrorAction SilentlyContinue
Remove-Item -Path ".\build" -Recurse -Force -ErrorAction SilentlyContinue
Remove-Item -Path ".\app\build" -Recurse -Force -ErrorAction SilentlyContinue
Remove-Item -Path ".\.gradle" -Recurse -Force -ErrorAction SilentlyContinue

# Clean Android build cache
Remove-Item -Path "$env:LOCALAPPDATA\Android\Sdk\build-cache\*" -Recurse -Force -ErrorAction SilentlyContinue

Write-Host "Cleanup complete. Please restart Android Studio and rebuild the project." 