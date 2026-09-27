# Set environment variables
$env:JAVA_HOME = "C:\Program Files\Android\Android Studio\jbr"
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"
$env:ANDROID_HOME = "$env:LOCALAPPDATA\Android\Sdk"

# Project paths
$projectPath = "C:\Users\mouad\AndroidStudioProjects\NewBudgetApp"
$gradleWrapperPath = "$projectPath\gradle\wrapper"

# Create necessary directories
New-Item -ItemType Directory -Force -Path $gradleWrapperPath | Out-Null

# Download Gradle wrapper JAR
$wrapperJarUrl = "https://github.com/gradle/gradle/raw/master/gradle/wrapper/gradle-wrapper.jar"
$wrapperJarPath = "$gradleWrapperPath\gradle-wrapper.jar"
Invoke-WebRequest -Uri $wrapperJarUrl -OutFile $wrapperJarPath

# Create gradle-wrapper.properties
$propertiesContent = @"
distributionBase=GRADLE_USER_HOME
distributionPath=wrapper/dists
distributionUrl=https\://services.gradle.org/distributions/gradle-8.0-bin.zip
zipStoreBase=GRADLE_USER_HOME
zipStorePath=wrapper/dists
"@
Set-Content -Path "$gradleWrapperPath\gradle-wrapper.properties" -Value $propertiesContent

Write-Host "Project initialized. Starting Android Studio..."

# Launch Android Studio
Start-Process -FilePath "C:\Program Files\Android\Android Studio\bin\studio64.exe" -ArgumentList $projectPath 