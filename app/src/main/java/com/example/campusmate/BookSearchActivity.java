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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campusmate.adapters.BookAdapter;
import com.example.campusmate.db.DatabaseHelper;
import com.example.campusmate.models.Book;
import com.example.campusmate.network.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;

public class BookSearchActivity extends Activity {
    private EditText edtBookQuery;
    private TextView txtBookStatus;
    private ArrayList<Book> bookList;
    private BookAdapter adapter;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_search);

        dbHelper = new DatabaseHelper(this);
        edtBookQuery = findViewById(R.id.edtBookQuery);
        txtBookStatus = findViewById(R.id.txtBookStatus);
        Button btnSearchBooks = findViewById(R.id.btnSearchBooks);
        Button btnTopicJava = findViewById(R.id.btnTopicJava);
        Button btnTopicAndroid = findViewById(R.id.btnTopicAndroid);
        Button btnTopicDatabase = findViewById(R.id.btnTopicDatabase);
        Button btnViewSavedBooks = findViewById(R.id.btnViewSavedBooks);
        RecyclerView recyclerBooks = findViewById(R.id.recyclerBooks);

        bookList = new ArrayList<>();
        adapter = new BookAdapter(bookList, "Save", book -> {
            if (dbHelper.favoriteBookExists(book.getTitle(), book.getAuthor())) {
                Toast.makeText(this, "Already saved in favourites", Toast.LENGTH_SHORT).show();
            } else {
                dbHelper.insertFavoriteBook(book);
                Toast.makeText(this, "Book saved to favourites", Toast.LENGTH_SHORT).show();
            }
        });

        recyclerBooks.setLayoutManager(new LinearLayoutManager(this));
        recyclerBooks.setAdapter(adapter);

        btnSearchBooks.setOnClickListener(v -> searchBooks());
        btnTopicJava.setOnClickListener(v -> quickSearch("Java programming"));
        btnTopicAndroid.setOnClickListener(v -> quickSearch("Android development"));
        btnTopicDatabase.setOnClickListener(v -> quickSearch("Database systems"));
        btnViewSavedBooks.setOnClickListener(v -> startActivity(new Intent(this, FavoriteBooksActivity.class)));
    }

    private void quickSearch(String keyword) {
        edtBookQuery.setText(keyword);
        searchBooks();
    }

    private String encodeText(String value) {
        try {
            return URLEncoder.encode(value, "UTF-8");
        } catch (Exception e) {
            return value;
        }
    }

    private void searchBooks() {
        String query = edtBookQuery.getText().toString().trim();
        if (query.isEmpty()) {
            edtBookQuery.setError("Search text is required");
            edtBookQuery.requestFocus();
            return;
        }

        txtBookStatus.setText("Searching books...");
        bookList.clear();
        adapter.notifyDataSetChanged();

        String url = "https://openlibrary.org/search.json?q=" + encodeText(query) + "&limit=10";

        // Source: Open Library Search API - https://openlibrary.org/dev/docs/api/search
        // Purpose: search study-related books and save selected books in the local SQLite database.
        NetworkUtils.getJson(url, new NetworkUtils.JsonCallback() {
            @Override
            public void onSuccess(JSONObject object) {
                try {
                    JSONArray docs = object.optJSONArray("docs");
                    if (docs == null || docs.length() == 0) {
                        txtBookStatus.setText("No books found. Try another keyword.");
                        return;
                    }

                    for (int i = 0; i < docs.length(); i++) {
                        JSONObject item = docs.getJSONObject(i);
                        String title = item.optString("title", "Unknown title");

                        String author = "Unknown author";
                        JSONArray authorArray = item.optJSONArray("author_name");
                        if (authorArray != null && authorArray.length() > 0) {
                            author = authorArray.getString(0);
                        }

                        String year = item.has("first_publish_year")
                                ? String.valueOf(item.optInt("first_publish_year"))
                                : "Not available";

                        bookList.add(new Book(title, author, year));
                    }

                    adapter.notifyDataSetChanged();
                    txtBookStatus.setText(bookList.size() + " results found. Tap Save to store useful books in SQLite.");
                } catch (Exception e) {
                    txtBookStatus.setText("Could not read book data: " + e.getMessage());
                }
            }

            @Override
            public void onError(String message) {
                txtBookStatus.setText(message);
            }
        });
    }
}
