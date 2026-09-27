# Kill any running Android Studio processes
Get-Process studio64 -ErrorAction SilentlyContinue | Stop-Process -Force

# Project paths
$projectPath = "C:\Users\mouad\AndroidStudioProjects\NewBudgetApp"

Write-Host "1. Cleaning Gradle caches..."
Remove-Item -Path "$env:USERPROFILE\.gradle\caches" -Recurse -Force -ErrorAction SilentlyContinue
Remove-Item -Path "$projectPath\.gradle" -Recurse -Force -ErrorAction SilentlyContinue
Remove-Item -Path "$projectPath\build" -Recurse -Force -ErrorAction SilentlyContinue
Remove-Item -Path "$projectPath\app\build" -Recurse -Force -ErrorAction SilentlyContinue

Write-Host "2. Setting up Java environment..."
$env:JAVA_HOME = "C:\Program Files\Android\Android Studio\jbr"
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"

Write-Host "3. Updating gradle.properties..."
$gradleProps = @"
# Project-wide Gradle settings
org.gradle.jvmargs=-Xmx4096m -Dfile.encoding=UTF-8 -XX:+UseParallelGC
org.gradle.parallel=true
org.gradle.daemon=true
org.gradle.caching=true

# Android settings
android.useAndroidX=true
android.enableJetifier=true
android.nonTransitiveRClass=true

# Java settings
org.gradle.java.home=C:\\Program Files\\Android\\Android Studio\\jbr
"@
Set-Content -Path "$projectPath\gradle.properties" -Value $gradleProps

Write-Host "4. Updating local.properties..."
$sdkPath = "$env:LOCALAPPDATA\Android\Sdk"
$localProps = "sdk.dir=$($sdkPath -replace '\\', '\\')"
Set-Content -Path "$projectPath\local.properties" -Value $localProps

Write-Host "5. Starting Android Studio..."
Start-Process -FilePath "C:\Program Files\Android\Android Studio\bin\studio64.exe" -ArgumentList $projectPath

Write-Host @"
Configuration updated! Please:
1. Wait for Android Studio to open
2. Click 'Trust Project' if prompted
3. Wait for Gradle sync to complete
4. If you see any SDK-related prompts, accept them
5. Once everything is synced, click the green 'Run' button
"@ 