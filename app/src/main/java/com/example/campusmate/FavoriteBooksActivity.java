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
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campusmate.adapters.BookAdapter;
import com.example.campusmate.db.DatabaseHelper;
import com.example.campusmate.models.Book;

import java.util.ArrayList;

public class FavoriteBooksActivity extends Activity {
    private DatabaseHelper dbHelper;
    private ArrayList<Book> bookList;
    private BookAdapter adapter;
    private TextView txtEmptyBooks, txtSavedBooksCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_books);

        dbHelper = new DatabaseHelper(this);
        txtEmptyBooks = findViewById(R.id.txtEmptyBooks);
        txtSavedBooksCount = findViewById(R.id.txtSavedBooksCount);
        RecyclerView recyclerFavorites = findViewById(R.id.recyclerFavorites);

        bookList = new ArrayList<>();
        adapter = new BookAdapter(bookList, "Delete", this::confirmDelete);
        recyclerFavorites.setLayoutManager(new LinearLayoutManager(this));
        recyclerFavorites.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFavoriteBooks();
    }

    private void loadFavoriteBooks() {
        bookList.clear();
        bookList.addAll(dbHelper.getAllFavoriteBooks());
        adapter.notifyDataSetChanged();
        txtEmptyBooks.setVisibility(bookList.isEmpty() ? View.VISIBLE : View.GONE);
        txtSavedBooksCount.setText("Saved books: " + bookList.size());
    }

    private void confirmDelete(Book book) {
        new AlertDialog.Builder(this)
                .setTitle("Delete favourite")
                .setMessage("Remove this book from favourites?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    dbHelper.deleteFavoriteBook(book.getId());
                    Toast.makeText(this, "Book removed", Toast.LENGTH_SHORT).show();
                    loadFavoriteBooks();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
