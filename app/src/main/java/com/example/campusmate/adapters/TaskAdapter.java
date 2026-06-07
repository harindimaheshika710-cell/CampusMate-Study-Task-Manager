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
package com.example.campusmate.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campusmate.R;
import com.example.campusmate.models.Task;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private final ArrayList<Task> taskList;
    private final TaskActionListener listener;

    public interface TaskActionListener {
        void onEdit(Task task);
        void onDelete(Task task);
        void onComplete(Task task);
    }

    public TaskAdapter(ArrayList<Task> taskList, TaskActionListener listener) {
        this.taskList = taskList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.txtTaskTitle.setText(task.getTitle());
        holder.txtTaskDetails.setText("📘 " + task.getSubject() + "  •  📅 Due: " + task.getDueDate());
        holder.txtTaskMeta.setText(getPriorityIcon(task.getPriority()) + " Priority: " + task.getPriority() + "  •  " + getStatusIcon(task.getStatus()) + " Status: " + task.getStatus());

        if ("Completed".equalsIgnoreCase(task.getStatus())) {
            holder.btnComplete.setEnabled(false);
            holder.btnComplete.setText("Done");
        } else {
            holder.btnComplete.setEnabled(true);
            holder.btnComplete.setText("Complete");
        }

        holder.btnEdit.setOnClickListener(v -> listener.onEdit(task));
        holder.btnDelete.setOnClickListener(v -> listener.onDelete(task));
        holder.btnComplete.setOnClickListener(v -> listener.onComplete(task));
    }


    private String getPriorityIcon(String priority) {
        if ("High".equalsIgnoreCase(priority)) return "🔴";
        if ("Medium".equalsIgnoreCase(priority)) return "🟠";
        return "🟢";
    }

    private String getStatusIcon(String status) {
        if ("Completed".equalsIgnoreCase(status)) return "✅";
        return "⏳";
    }
    @Override
    public int getItemCount() {
        return taskList.size();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView txtTaskTitle, txtTaskDetails, txtTaskMeta;
        Button btnEdit, btnDelete, btnComplete;

        TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTaskTitle = itemView.findViewById(R.id.txtTaskTitle);
            txtTaskDetails = itemView.findViewById(R.id.txtTaskDetails);
            txtTaskMeta = itemView.findViewById(R.id.txtTaskMeta);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnComplete = itemView.findViewById(R.id.btnComplete);
        }
    }
}
