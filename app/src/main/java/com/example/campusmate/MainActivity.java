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
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.campusmate.db.DatabaseHelper;

public class MainActivity extends Activity {
    private DatabaseHelper dbHelper;
    private TextView txtTotalTasks, txtPendingTasks, txtCompletedTasks, txtSavedBooks, txtProgressMessage, txtDashboardPercent;
    private ProgressBar progressDashboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);

        Button btnTasks = findViewById(R.id.btnTasks);
        Button btnWeather = findViewById(R.id.btnWeather);
        Button btnBooks = findViewById(R.id.btnBooks);
        Button btnFavorites = findViewById(R.id.btnFavorites);
        Button btnFocusTimer = findViewById(R.id.btnFocusTimer);
        Button btnAbout = findViewById(R.id.btnAbout);

        txtTotalTasks = findViewById(R.id.txtTotalTasks);
        txtPendingTasks = findViewById(R.id.txtPendingTasks);
        txtCompletedTasks = findViewById(R.id.txtCompletedTasks);
        txtSavedBooks = findViewById(R.id.txtSavedBooks);
        txtProgressMessage = findViewById(R.id.txtProgressMessage);
        txtDashboardPercent = findViewById(R.id.txtDashboardPercent);
        progressDashboard = findViewById(R.id.progressDashboard);

        btnTasks.setOnClickListener(v -> startActivity(new Intent(this, TaskActivity.class)));
        btnWeather.setOnClickListener(v -> startActivity(new Intent(this, WeatherActivity.class)));
        btnBooks.setOnClickListener(v -> startActivity(new Intent(this, BookSearchActivity.class)));
        btnFavorites.setOnClickListener(v -> startActivity(new Intent(this, FavoriteBooksActivity.class)));
        btnFocusTimer.setOnClickListener(v -> startActivity(new Intent(this, StudyTipsActivity.class)));
        btnAbout.setOnClickListener(v -> startActivity(new Intent(this, AboutActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateDashboardStats();
    }

    private void updateDashboardStats() {
        int total = dbHelper.getTaskCount();
        int pending = dbHelper.getTaskCountByStatus("Pending");
        int completed = dbHelper.getTaskCountByStatus("Completed");
        int savedBooks = dbHelper.getFavoriteBookCount();

        txtTotalTasks.setText(String.valueOf(total));
        txtPendingTasks.setText(String.valueOf(pending));
        txtCompletedTasks.setText(String.valueOf(completed));
        txtSavedBooks.setText(String.valueOf(savedBooks));

        int progress = total == 0 ? 0 : (completed * 100) / total;
        progressDashboard.setProgress(progress);
        txtDashboardPercent.setText(progress + "%");

        if (total == 0) {
            txtProgressMessage.setText("Start strong: add your first task, check weather, then use the focus timer.");
        } else if (progress == 100) {
            txtProgressMessage.setText("Excellent! All study tasks are completed. Review saved books next.");
        } else {
            txtProgressMessage.setText("Study progress: " + progress + "% completed. Keep going with one focused session.");
        }
    }
}
