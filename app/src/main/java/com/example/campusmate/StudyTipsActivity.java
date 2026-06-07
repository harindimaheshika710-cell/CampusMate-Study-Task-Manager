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
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class StudyTipsActivity extends Activity {
    private TextView txtTimer, txtTimerStatus, txtStudyTip;
    private ProgressBar progressFocus;
    private Button btnStartPause;
    private CountDownTimer timer;
    private long selectedDurationMillis = 25 * 60 * 1000L;
    private long remainingMillis = selectedDurationMillis;
    private boolean running = false;

    private final String[] studyTips = new String[]{
            "Break a large assignment into small 25-minute study blocks.",
            "Start with the hardest task first, then move to easier work.",
            "Keep your phone away during the focus session to avoid distractions.",
            "After every study block, write one short summary of what you learned.",
            "Use the book finder to save useful references before writing reports.",
            "Check tomorrow's weather before planning your campus travel time.",
            "Complete one small task today instead of waiting for perfect motivation."
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_tips);

        txtTimer = findViewById(R.id.txtTimer);
        txtTimerStatus = findViewById(R.id.txtTimerStatus);
        txtStudyTip = findViewById(R.id.txtStudyTip);
        progressFocus = findViewById(R.id.progressFocus);
        Button btnModeFocus = findViewById(R.id.btnModeFocus);
        Button btnModeShort = findViewById(R.id.btnModeShort);
        Button btnModeQuick = findViewById(R.id.btnModeQuick);
        btnStartPause = findViewById(R.id.btnStartPause);
        Button btnResetTimer = findViewById(R.id.btnResetTimer);
        Button btnNewTip = findViewById(R.id.btnNewTip);

        progressFocus.setMax(100);
        showRandomTip();
        updateTimerText();

        btnModeFocus.setOnClickListener(v -> setTimerMode(25, "Focus mode selected"));
        btnModeShort.setOnClickListener(v -> setTimerMode(5, "Short break selected"));
        btnModeQuick.setOnClickListener(v -> setTimerMode(1, "Quick demo mode selected"));
        btnStartPause.setOnClickListener(v -> {
            if (running) {
                pauseTimer();
                btnStartPause.setText("▶ Start Focus");
            } else {
                startTimer();
                btnStartPause.setText("Pause");
            }
        });
        btnResetTimer.setOnClickListener(v -> {
            resetTimer();
            btnStartPause.setText("▶ Start Focus");
        });
        btnNewTip.setOnClickListener(v -> showRandomTip());
    }

    private void setTimerMode(int minutes, String message) {
        if (running) {
            pauseTimer();
        }
        selectedDurationMillis = minutes * 60 * 1000L;
        remainingMillis = selectedDurationMillis;
        txtTimerStatus.setText(message);
        updateTimerText();
    }

    private void startTimer() {
        running = true;
        txtTimerStatus.setText("Focus session running. Stay with one task.");
        timer = new CountDownTimer(remainingMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                remainingMillis = millisUntilFinished;
                updateTimerText();
            }

            @Override
            public void onFinish() {
                running = false;
                remainingMillis = 0;
                updateTimerText();
                txtTimerStatus.setText("Session complete! Take a short break.");
                btnStartPause.setText("▶ Start Focus");
                Toast.makeText(StudyTipsActivity.this, "Focus session complete", Toast.LENGTH_LONG).show();
            }
        }.start();
    }

    private void pauseTimer() {
        running = false;
        if (timer != null) {
            timer.cancel();
        }
        txtTimerStatus.setText("Timer paused. Continue when you are ready.");
    }

    private void resetTimer() {
        if (timer != null) {
            timer.cancel();
        }
        running = false;
        remainingMillis = selectedDurationMillis;
        txtTimerStatus.setText("Timer reset. Choose a mode and start studying.");
        updateTimerText();
    }

    private void updateTimerText() {
        long totalSeconds = remainingMillis / 1000;
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        txtTimer.setText(String.format("%02d:%02d", minutes, seconds));
        int progress = selectedDurationMillis == 0 ? 0 : (int) ((selectedDurationMillis - remainingMillis) * 100 / selectedDurationMillis);
        progressFocus.setProgress(progress);
    }

    private void showRandomTip() {
        int index = new Random().nextInt(studyTips.length);
        txtStudyTip.setText("💡 " + studyTips[index]);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }
}
