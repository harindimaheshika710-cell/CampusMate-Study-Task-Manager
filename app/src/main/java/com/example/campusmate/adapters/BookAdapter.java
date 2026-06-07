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
import com.example.campusmate.models.Book;

import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    private final ArrayList<Book> bookList;
    private final String buttonText;
    private final BookActionListener listener;

    public interface BookActionListener {
        void onAction(Book book);
    }

    public BookAdapter(ArrayList<Book> bookList, String buttonText, BookActionListener listener) {
        this.bookList = bookList;
        this.buttonText = buttonText;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = bookList.get(position);
        holder.txtBookTitle.setText(book.getTitle());
        holder.txtBookAuthor.setText("Author: " + book.getAuthor());
        holder.txtBookYear.setText("First published: " + book.getPublishYear());
        holder.btnBookAction.setText(buttonText);
        holder.btnBookAction.setOnClickListener(v -> listener.onAction(book));
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    static class BookViewHolder extends RecyclerView.ViewHolder {
        TextView txtBookTitle, txtBookAuthor, txtBookYear;
        Button btnBookAction;

        BookViewHolder(@NonNull View itemView) {
            super(itemView);
            txtBookTitle = itemView.findViewById(R.id.txtBookTitle);
            txtBookAuthor = itemView.findViewById(R.id.txtBookAuthor);
            txtBookYear = itemView.findViewById(R.id.txtBookYear);
            btnBookAction = itemView.findViewById(R.id.btnBookAction);
        }
    }
}
