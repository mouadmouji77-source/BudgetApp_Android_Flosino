# Set environment variables
$env:JAVA_HOME = "C:\Program Files\Android\Android Studio\jbr"
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"
$env:ANDROID_HOME = "$env:LOCALAPPDATA\Android\Sdk"

# Change to project directory
Set-Location "C:\Users\mouad\AndroidStudioProjects\NewBudgetApp"

# Clean and build the project
Write-Host "Building the project..."
.\gradlew.bat clean
.\gradlew.bat build

# Run the app
Write-Host "Starting Android Studio..."
Start-Process -FilePath "C:\Program Files\Android\Android Studio\bin\studio64.exe" -ArgumentList $PWD.Path 