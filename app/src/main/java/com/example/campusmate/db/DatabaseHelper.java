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
package com.example.campusmate.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.campusmate.models.Book;
import com.example.campusmate.models.Task;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "campusmate.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_TASKS = "tasks";
    private static final String TASK_ID = "id";
    private static final String TASK_SUBJECT = "subject";
    private static final String TASK_TITLE = "title";
    private static final String TASK_DUE_DATE = "due_date";
    private static final String TASK_PRIORITY = "priority";
    private static final String TASK_STATUS = "status";

    private static final String TABLE_BOOKS = "favorite_books";
    private static final String BOOK_ID = "id";
    private static final String BOOK_TITLE = "book_title";
    private static final String BOOK_AUTHOR = "author";
    private static final String BOOK_YEAR = "publish_year";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQLite source note: database CRUD pattern based on official Android SQLiteOpenHelper documentation.
        String createTasks = "CREATE TABLE " + TABLE_TASKS + "(" +
                TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TASK_SUBJECT + " TEXT NOT NULL, " +
                TASK_TITLE + " TEXT NOT NULL, " +
                TASK_DUE_DATE + " TEXT NOT NULL, " +
                TASK_PRIORITY + " TEXT NOT NULL, " +
                TASK_STATUS + " TEXT NOT NULL)";

        String createBooks = "CREATE TABLE " + TABLE_BOOKS + "(" +
                BOOK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                BOOK_TITLE + " TEXT NOT NULL, " +
                BOOK_AUTHOR + " TEXT NOT NULL, " +
                BOOK_YEAR + " TEXT NOT NULL)";

        db.execSQL(createTasks);
        db.execSQL(createBooks);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKS);
        onCreate(db);
    }

    public long insertTask(Task task) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TASK_SUBJECT, task.getSubject());
        values.put(TASK_TITLE, task.getTitle());
        values.put(TASK_DUE_DATE, task.getDueDate());
        values.put(TASK_PRIORITY, task.getPriority());
        values.put(TASK_STATUS, task.getStatus());
        long result = db.insert(TABLE_TASKS, null, values);
        db.close();
        return result;
    }

    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_TASKS, null, null, null, null, null, TASK_ID + " DESC");
        if (cursor.moveToFirst()) {
            do {
                tasks.add(new Task(
                        cursor.getInt(cursor.getColumnIndexOrThrow(TASK_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(TASK_SUBJECT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(TASK_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(TASK_DUE_DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(TASK_PRIORITY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(TASK_STATUS))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return tasks;
    }

    public Task getTaskById(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_TASKS, null, TASK_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
        Task task = null;
        if (cursor.moveToFirst()) {
            task = new Task(
                    cursor.getInt(cursor.getColumnIndexOrThrow(TASK_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(TASK_SUBJECT)),
                    cursor.getString(cursor.getColumnIndexOrThrow(TASK_TITLE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(TASK_DUE_DATE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(TASK_PRIORITY)),
                    cursor.getString(cursor.getColumnIndexOrThrow(TASK_STATUS))
            );
        }
        cursor.close();
        db.close();
        return task;
    }

    public int updateTask(Task task) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TASK_SUBJECT, task.getSubject());
        values.put(TASK_TITLE, task.getTitle());
        values.put(TASK_DUE_DATE, task.getDueDate());
        values.put(TASK_PRIORITY, task.getPriority());
        values.put(TASK_STATUS, task.getStatus());
        int result = db.update(TABLE_TASKS, values, TASK_ID + "=?", new String[]{String.valueOf(task.getId())});
        db.close();
        return result;
    }

    public int updateTaskStatus(int id, String status) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TASK_STATUS, status);
        int result = db.update(TABLE_TASKS, values, TASK_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return result;
    }

    public int deleteTask(int id) {
        SQLiteDatabase db = getWritableDatabase();
        int result = db.delete(TABLE_TASKS, TASK_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return result;
    }

    public int getTaskCount() {
        return getCount("SELECT COUNT(*) FROM " + TABLE_TASKS, null);
    }

    public int getTaskCountByStatus(String status) {
        return getCount("SELECT COUNT(*) FROM " + TABLE_TASKS + " WHERE " + TASK_STATUS + "=?", new String[]{status});
    }

    public long insertFavoriteBook(Book book) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BOOK_TITLE, book.getTitle());
        values.put(BOOK_AUTHOR, book.getAuthor());
        values.put(BOOK_YEAR, book.getPublishYear());
        long result = db.insert(TABLE_BOOKS, null, values);
        db.close();
        return result;
    }

    public ArrayList<Book> getAllFavoriteBooks() {
        ArrayList<Book> books = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_BOOKS, null, null, null, null, null, BOOK_ID + " DESC");
        if (cursor.moveToFirst()) {
            do {
                books.add(new Book(
                        cursor.getInt(cursor.getColumnIndexOrThrow(BOOK_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(BOOK_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(BOOK_AUTHOR)),
                        cursor.getString(cursor.getColumnIndexOrThrow(BOOK_YEAR))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return books;
    }

    public int deleteFavoriteBook(int id) {
        SQLiteDatabase db = getWritableDatabase();
        int result = db.delete(TABLE_BOOKS, BOOK_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return result;
    }

    public int getFavoriteBookCount() {
        return getCount("SELECT COUNT(*) FROM " + TABLE_BOOKS, null);
    }

    public boolean favoriteBookExists(String title, String author) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_BOOKS, new String[]{BOOK_ID}, BOOK_TITLE + "=? AND " + BOOK_AUTHOR + "=?",
                new String[]{title, author}, null, null, null);
        boolean exists = cursor.moveToFirst();
        cursor.close();
        db.close();
        return exists;
    }

    private int getCount(String sql, String[] args) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, args);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return count;
    }
}
