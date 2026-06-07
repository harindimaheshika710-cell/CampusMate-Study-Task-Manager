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
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.campusmate.db.DatabaseHelper;
import com.example.campusmate.models.Task;

import java.util.Calendar;
import java.util.Locale;

public class AddEditTaskActivity extends Activity {
    private EditText edtSubject, edtTitle, edtDueDate;
    private Spinner spinnerPriority, spinnerStatus;
    private DatabaseHelper dbHelper;
    private int taskId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_task);

        dbHelper = new DatabaseHelper(this);
        TextView txtFormTitle = findViewById(R.id.txtFormTitle);
        edtSubject = findViewById(R.id.edtSubject);
        edtTitle = findViewById(R.id.edtTitle);
        edtDueDate = findViewById(R.id.edtDueDate);
        spinnerPriority = findViewById(R.id.spinnerPriority);
        spinnerStatus = findViewById(R.id.spinnerStatus);
        Button btnSaveTask = findViewById(R.id.btnSaveTask);

        setupSpinner(spinnerPriority, new String[]{"High", "Medium", "Low"});
        setupSpinner(spinnerStatus, new String[]{"Pending", "Completed"});

        edtDueDate.setFocusable(false);
        edtDueDate.setOnClickListener(v -> showDatePicker());

        taskId = getIntent().getIntExtra("task_id", -1);
        if (taskId != -1) {
            txtFormTitle.setText("Edit Study Task");
            loadTaskForEdit(taskId);
        }

        btnSaveTask.setOnClickListener(v -> saveTask());
    }

    private void setupSpinner(Spinner spinner, String[] items) {
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            String date = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);
            edtDueDate.setText(date);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private void loadTaskForEdit(int id) {
        Task task = dbHelper.getTaskById(id);
        if (task != null) {
            edtSubject.setText(task.getSubject());
            edtTitle.setText(task.getTitle());
            edtDueDate.setText(task.getDueDate());
            setSpinnerSelection(spinnerPriority, task.getPriority());
            setSpinnerSelection(spinnerStatus, task.getStatus());
        }
    }

    private void setSpinnerSelection(Spinner spinner, String value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(value)) {
                spinner.setSelection(i);
                break;
            }
        }
    }

    private void saveTask() {
        String subject = edtSubject.getText().toString().trim();
        String title = edtTitle.getText().toString().trim();
        String dueDate = edtDueDate.getText().toString().trim();
        String priority = spinnerPriority.getSelectedItem().toString();
        String status = spinnerStatus.getSelectedItem().toString();

        if (subject.isEmpty()) {
            edtSubject.setError("Subject is required");
            edtSubject.requestFocus();
            return;
        }

        if (title.isEmpty()) {
            edtTitle.setError("Task title is required");
            edtTitle.requestFocus();
            return;
        }

        if (dueDate.isEmpty()) {
            edtDueDate.setError("Due date is required");
            edtDueDate.requestFocus();
            return;
        }

        Task task = new Task(subject, title, dueDate, priority, status);
        if (taskId == -1) {
            dbHelper.insertTask(task);
            Toast.makeText(this, "Task saved successfully", Toast.LENGTH_SHORT).show();
        } else {
            task.setId(taskId);
            dbHelper.updateTask(task);
            Toast.makeText(this, "Task updated successfully", Toast.LENGTH_SHORT).show();
        }
        finish();
    }
}
