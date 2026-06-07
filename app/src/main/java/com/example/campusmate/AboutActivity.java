/*
 * SOURCE ACKNOWLEDGEMENT REQUIRED BY ASSIGNMENT
 * Project: CampusMate - Study Task Manager mobile application.
 * This file was created for this assignment by the project team with AI code support from
 * OpenAI ChatGPT: https://chat.openai.com/
 * Android implementation concepts referenced from official Android Developers documentation:
 * - Activity / app components: https://developer.android.com/guide/components/activities
 * - SQLite data storage: https://developer.android.com/training/data-storage/sqlite
 * - RecyclerView list UI: https://developer.android.com/develop/ui/views/layout/recyclerview
 * - CountDownTimer: https://developer.android.com/reference/android/os/CountDownTimer
 * External API documentation used in this project:
 * - Open-Meteo Geocoding API: https://open-meteo.com/en/docs/geocoding-api
 * - Open-Meteo Forecast API: https://open-meteo.com/en/docs
 * - Open Library Search API: https://openlibrary.org/dev/docs/api/search
 * If a group member edits or owns this file, add their real name/student ID in
 * INDIVIDUAL_CONTRIBUTIONS.txt before submission.
 */
package com.example.campusmate;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class AboutActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        TextView txtAbout = findViewById(R.id.txtAbout);
        txtAbout.setText(
                "CampusMate - Smart Student Study Planner\n\n" +
                "Assignment Requirement Coverage\n" +
                "✅ Android Studio mobile app\n" +
                "✅ Java language\n" +
                "✅ SQLite database for storage\n" +
                "✅ Database write: add/update/delete tasks and save books\n" +
                "✅ Database read: task list, dashboard stats and saved books\n" +
                "✅ Minimum two external APIs used\n" +
                "✅ Separate function for each member\n\n" +
                "Main Features\n" +
                "1. Study Task Manager - add, view, edit, delete, complete and filter study tasks.\n" +
                "2. Weather Assistant - search a city, read current weather and 3-day forecast for campus travel planning.\n" +
                "3. Study Book Finder - search study books using Open Library and save useful books.\n" +
                "4. Saved Books - view and delete favourite books stored in SQLite.\n" +
                "5. Focus Timer - Pomodoro-style timer with smart study tips.\n" +
                "6. Dashboard - shows task counts, completion progress and saved book count.\n\n" +
                "External API Sources\n" +
                "• Open-Meteo Geocoding API: https://open-meteo.com/en/docs/geocoding-api\n" +
                "• Open-Meteo Forecast API: https://open-meteo.com/en/docs\n" +
                "• Open Library Search API: https://openlibrary.org/dev/docs/api/search\n\n" +
                "Source Code Acknowledgement\n" +
                "This project was created for the assignment with assistance from OpenAI ChatGPT. " +
                "AI-assisted source comments have been added at the top of Java source files and in important API sections. " +
                "Android/SQLite/RecyclerView concepts are based on official Android developer documentation.\n\n" +
                "Functionality Split\n" +
                "• Study Task Manager: SQLite task CRUD, filters, search and progress summary\n" +
                "• Weather Assistant: Open-Meteo API integration and travel suggestion\n" +
                "• Study Book Finder: Open Library API, search results and saved books\n" +
                "• Focus Timer: Pomodoro-style timer and random study tips\n\n" +
                "HCI Principles Applied\n" +
                "• Simple home dashboard\n" +
                "• Consistent colours and rounded cards\n" +
                "• Readable labels and helpful messages\n" +
                "• Confirmation before deleting data\n" +
                "• Compact navigation suitable for small phone screens\n" +
                "• Quick topic buttons and city buttons for faster interaction\n" +
                "• Progress bars and compact feature grid for a more advanced UI\n"
        );
    }
}
