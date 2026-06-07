# HCI Mobile App - CampusMate

CampusMate is a Java Android Studio mobile app for the HCI Assignment 02.

## Main Requirements Covered
- Android Studio project
- Java language
- SQLite database read/write
- Minimum two external APIs
- RecyclerView usage
- Separate member functions
- Source acknowledgement comments in code
- Special run instructions included

## First Run
1. Extract ZIP.
2. Open the main project folder in Android Studio.
3. Do not open only the app folder.
4. Click **Sync Now**.
5. Use JDK 17 / Embedded JDK.
6. Run on emulator or Android phone.

## PowerShell Build
```powershell
.\gradlew.bat clean :app:assembleDebug
```

## API Sources
- Open-Meteo Geocoding API: https://open-meteo.com/en/docs/geocoding-api
- Open-Meteo Forecast API: https://open-meteo.com/en/docs
- Open Library Search API: https://openlibrary.org/dev/docs/api/search

## AI / Source Acknowledgement
This project was prepared with AI support from OpenAI ChatGPT and references official Android Developer documentation. Source acknowledgement comments are included in Java, XML, Manifest and Gradle files.
