$studioPath = "C:\Program Files\Android\Android Studio\bin\studio64.exe"
$projectPath = "C:\Users\mouad\AndroidStudioProjects\NewBudgetApp"

# Set environment variables
$env:JAVA_HOME = "C:\Program Files\Android\Android Studio\jbr"
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"

# Launch Android Studio
Start-Process -FilePath $studioPath -ArgumentList $projectPath 