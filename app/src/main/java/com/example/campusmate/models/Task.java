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
package com.example.campusmate.models;

public class Task {
    private int id;
    private String subject;
    private String title;
    private String dueDate;
    private String priority;
    private String status;

    public Task(int id, String subject, String title, String dueDate, String priority, String status) {
        this.id = id;
        this.subject = subject;
        this.title = title;
        this.dueDate = dueDate;
        this.priority = priority;
        this.status = status;
    }

    public Task(String subject, String title, String dueDate, String priority, String status) {
        this.subject = subject;
        this.title = title;
        this.dueDate = dueDate;
        this.priority = priority;
        this.status = status;
    }

    public int getId() { return id; }
    public String getSubject() { return subject; }
    public String getTitle() { return title; }
    public String getDueDate() { return dueDate; }
    public String getPriority() { return priority; }
    public String getStatus() { return status; }

    public void setId(int id) { this.id = id; }
    public void setSubject(String subject) { this.subject = subject; }
    public void setTitle(String title) { this.title = title; }
    public void setDueDate(String dueDate) { this.dueDate = dueDate; }
    public void setPriority(String priority) { this.priority = priority; }
    public void setStatus(String status) { this.status = status; }
}
