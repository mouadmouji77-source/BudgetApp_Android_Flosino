# Function to check if a path exists
function Test-JavaPath {
    param([string]$path)
    if (Test-Path $path) {
        Write-Host "Found Java at: $path"
        return $true
    }
    return $false
}

# Project paths
$projectPath = "C:\Users\mouad\AndroidStudioProjects\NewBudgetApp"
$gradleWrapperPath = "$projectPath\gradle\wrapper"

Write-Host "1. Setting up directories..."
# Create necessary directories
New-Item -ItemType Directory -Force -Path $gradleWrapperPath | Out-Null

Write-Host "2. Finding valid Java installation..."
# Possible Java paths
$javaPaths = @(
    "C:\Program Files\Android\Android Studio\jbr",
    "C:\Program Files\Android\Android Studio\jre",
    "${env:ProgramFiles}\Java\jdk-17",
    "${env:ProgramFiles}\Eclipse Adoptium\jdk-17"
)

# Find valid Java path
$validJavaPath = $null
foreach ($path in $javaPaths) {
    if (Test-JavaPath $path) {
        $validJavaPath = $path
        break
    }
}

if ($null -eq $validJavaPath) {
    Write-Host "No valid Java installation found!"
    exit 1
}

Write-Host "3. Configuring Gradle..."
# Download Gradle wrapper JAR
$wrapperJarUrl = "https://github.com/gradle/gradle/raw/master/gradle/wrapper/gradle-wrapper.jar"
$wrapperJarPath = "$gradleWrapperPath\gradle-wrapper.jar"
Invoke-WebRequest -Uri $wrapperJarUrl -OutFile $wrapperJarPath

# Create gradle-wrapper.properties
$wrapperProps = @"
distributionBase=GRADLE_USER_HOME
distributionPath=wrapper/dists
distributionUrl=https\://services.gradle.org/distributions/gradle-8.0-bin.zip
zipStoreBase=GRADLE_USER_HOME
zipStorePath=wrapper/dists
"@
Set-Content -Path "$gradleWrapperPath\gradle-wrapper.properties" -Value $wrapperProps

# Update gradle.properties
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
org.gradle.java.home=$($validJavaPath -replace '\\', '\\\\')
"@
Set-Content -Path "$projectPath\gradle.properties" -Value $gradleProps

Write-Host "4. Setting environment variables..."
# Set environment variables
$env:JAVA_HOME = $validJavaPath
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"
$env:ANDROID_HOME = "$env:LOCALAPPDATA\Android\Sdk"

Write-Host "5. Creating local.properties..."
# Create local.properties with SDK path
$localProps = "sdk.dir=$($env:ANDROID_HOME -replace '\\', '\\')"
Set-Content -Path "$projectPath\local.properties" -Value $localProps

Write-Host "6. Starting Android Studio..."
# Launch Android Studio
Start-Process -FilePath "C:\Program Files\Android\Android Studio\bin\studio64.exe" -ArgumentList $projectPath

Write-Host @"
Setup complete! Please:
1. Wait for Android Studio to open
2. Click 'Trust Project' if prompted
3. Wait for Gradle sync to complete
4. If you see any SDK-related prompts, accept them
5. Once everything is synced, click the green 'Run' button
"@ 