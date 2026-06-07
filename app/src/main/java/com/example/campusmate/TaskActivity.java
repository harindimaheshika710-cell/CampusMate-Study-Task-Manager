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
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campusmate.adapters.TaskAdapter;
import com.example.campusmate.db.DatabaseHelper;
import com.example.campusmate.models.Task;

import java.util.ArrayList;

public class TaskActivity extends Activity {
    private DatabaseHelper dbHelper;
    private ArrayList<Task> allTasks;
    private ArrayList<Task> visibleTasks;
    private TaskAdapter adapter;
    private TextView txtEmptyTasks, txtTaskSummary;
    private EditText edtTaskSearch;
    private ProgressBar progressTaskCompletion;
    private String activeFilter = "All";
    private String searchText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        dbHelper = new DatabaseHelper(this);
        txtEmptyTasks = findViewById(R.id.txtEmptyTasks);
        txtTaskSummary = findViewById(R.id.txtTaskSummary);
        edtTaskSearch = findViewById(R.id.edtTaskSearch);
        progressTaskCompletion = findViewById(R.id.progressTaskCompletion);
        Button btnAddTask = findViewById(R.id.btnAddTask);
        Button btnDemoTasks = findViewById(R.id.btnDemoTasks);
        Button btnShowAll = findViewById(R.id.btnShowAll);
        Button btnShowPending = findViewById(R.id.btnShowPending);
        Button btnShowCompleted = findViewById(R.id.btnShowCompleted);
        RecyclerView recyclerTasks = findViewById(R.id.recyclerTasks);

        allTasks = new ArrayList<>();
        visibleTasks = new ArrayList<>();
        adapter = new TaskAdapter(visibleTasks, new TaskAdapter.TaskActionListener() {
            @Override
            public void onEdit(Task task) {
                Intent intent = new Intent(TaskActivity.this, AddEditTaskActivity.class);
                intent.putExtra("task_id", task.getId());
                startActivity(intent);
            }

            @Override
            public void onDelete(Task task) {
                confirmDelete(task);
            }

            @Override
            public void onComplete(Task task) {
                dbHelper.updateTaskStatus(task.getId(), "Completed");
                Toast.makeText(TaskActivity.this, "Task marked as completed", Toast.LENGTH_SHORT).show();
                loadTasks();
            }
        });

        recyclerTasks.setLayoutManager(new LinearLayoutManager(this));
        recyclerTasks.setAdapter(adapter);

        edtTaskSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchText = s.toString().trim().toLowerCase();
                applyFilter();
                updateSummary();
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        btnAddTask.setOnClickListener(v -> startActivity(new Intent(this, AddEditTaskActivity.class)));
        btnDemoTasks.setOnClickListener(v -> addDemoTasks());
        btnShowAll.setOnClickListener(v -> changeFilter("All"));
        btnShowPending.setOnClickListener(v -> changeFilter("Pending"));
        btnShowCompleted.setOnClickListener(v -> changeFilter("Completed"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTasks();
    }

    private void loadTasks() {
        allTasks.clear();
        allTasks.addAll(dbHelper.getAllTasks());
        applyFilter();
        updateSummary();
    }

    private void changeFilter(String filter) {
        activeFilter = filter;
        applyFilter();
        updateSummary();
    }

    private void applyFilter() {
        visibleTasks.clear();
        for (Task task : allTasks) {
            boolean matchesFilter = "All".equals(activeFilter) || activeFilter.equalsIgnoreCase(task.getStatus());
            boolean matchesSearch = searchText.isEmpty()
                    || task.getTitle().toLowerCase().contains(searchText)
                    || task.getSubject().toLowerCase().contains(searchText)
                    || task.getPriority().toLowerCase().contains(searchText)
                    || task.getDueDate().toLowerCase().contains(searchText);
            if (matchesFilter && matchesSearch) {
                visibleTasks.add(task);
            }
        }
        adapter.notifyDataSetChanged();
        txtEmptyTasks.setVisibility(visibleTasks.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void updateSummary() {
        int total = allTasks.size();
        int pending = 0;
        int completed = 0;
        for (Task task : allTasks) {
            if ("Completed".equalsIgnoreCase(task.getStatus())) {
                completed++;
            } else {
                pending++;
            }
        }
        int progress = total == 0 ? 0 : (completed * 100) / total;
        progressTaskCompletion.setProgress(progress);
        txtTaskSummary.setText("Total: " + total + "  •  Pending: " + pending + "  •  Completed: " + completed + "  •  Progress: " + progress + "%  •  Filter: " + activeFilter + "  •  Showing: " + visibleTasks.size());
    }

    private void addDemoTasks() {
        dbHelper.insertTask(new Task("Mobile App Development", "Complete app UI testing", "2026-06-10", "High", "Pending"));
        dbHelper.insertTask(new Task("Database Systems", "Revise SQLite CRUD operations", "2026-06-12", "Medium", "Pending"));
        dbHelper.insertTask(new Task("HCI", "Prepare final demo script", "2026-06-14", "Low", "Completed"));
        Toast.makeText(this, "Demo tasks added to SQLite", Toast.LENGTH_SHORT).show();
        loadTasks();
    }

    private void confirmDelete(Task task) {
        new AlertDialog.Builder(this)
                .setTitle("Delete task")
                .setMessage("Do you want to delete this study task?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    dbHelper.deleteTask(task.getId());
                    Toast.makeText(this, "Task deleted", Toast.LENGTH_SHORT).show();
                    loadTasks();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
