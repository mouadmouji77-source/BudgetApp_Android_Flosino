# Function to check if a path exists
function Test-JavaPath {
    param([string]$path)
    if (Test-Path $path) {
        Write-Host "Found Java at: $path"
        return $true
    }
    return $false
}

# Possible Java paths
$javaPaths = @(
    "C:\Program Files\Android\Android Studio\jre",
    "C:\Program Files\Android\Android Studio\jbr",
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

# Update gradle.properties with correct Java path
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

Set-Content -Path "C:\Users\mouad\AndroidStudioProjects\NewBudgetApp\gradle.properties" -Value $gradleProps

# Set environment variables
$env:JAVA_HOME = $validJavaPath
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"

Write-Host "Starting Android Studio with Java from: $validJavaPath"
Start-Process -FilePath "C:\Program Files\Android\Android Studio\bin\studio64.exe" -ArgumentList "C:\Users\mouad\AndroidStudioProjects\NewBudgetApp" 