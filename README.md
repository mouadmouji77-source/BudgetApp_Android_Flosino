# Budget App

A simple Android application for tracking personal income and expenses.

## Features

- Add income and expense transactions
- View transaction history
- Track total balance
- View statistics
- Material Design UI

## Setup Instructions

1. Open Android Studio
2. Open the project by selecting `File > Open` and navigate to the `NewBudgetApp` directory
3. Wait for the project to sync and download dependencies
4. Connect an Android device or start an emulator
5. Click the "Run" button (green play icon) or press Shift+F10

## Development Environment

- Android Studio Hedgehog or later
- JDK 17
- Gradle 8.0
- Minimum SDK: Android 7.0 (API 24)
- Target SDK: Android 14 (API 34)

## Dependencies

- AndroidX AppCompat
- Material Design Components
- MPAndroidChart for statistics
- RecyclerView and CardView
- ViewBinding for view access
- Gson for data persistence

## Project Structure

```
app/
├── src/main/
│   ├── java/com/example/newbudgetapp/
│   │   ├── MainActivity.java
│   │   ├── Transaction.java
│   │   └── TransactionAdapter.java
│   ├── res/
│   │   ├── layout/
│   │   │   ├── activity_main.xml
│   │   │   └── item_transaction.xml
│   │   └── values/
│   │       ├── colors.xml
│   │       └── strings.xml
│   └── AndroidManifest.xml
├── build.gradle
└── proguard-rules.pro
``` 