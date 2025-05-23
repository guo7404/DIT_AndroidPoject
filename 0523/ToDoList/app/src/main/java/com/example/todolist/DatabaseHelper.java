package com.example.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TodoList.db";
    private static final int DATABASE_VERSION = 1;

    // Table name
    private static final String TABLE_TODO = "todo";

    // Column names
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TASK = "task";
    private static final String COLUMN_COMPLETED = "completed";
    private static final String COLUMN_ALARM_TIME = "alarm_time";

    // Create table SQL query
    private static final String CREATE_TABLE_TODO =
            "CREATE TABLE " + TABLE_TODO + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_TASK + " TEXT NOT NULL,"
                    + COLUMN_COMPLETED + " INTEGER DEFAULT 0,"
                    + COLUMN_ALARM_TIME + " INTEGER DEFAULT 0"
                    + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_TODO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);
        onCreate(db);
    }

    // Insert a todo item
    public long insertTodo(TodoItem todoItem) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK, todoItem.getTask());
        values.put(COLUMN_COMPLETED, todoItem.isCompleted() ? 1 : 0);
        values.put(COLUMN_ALARM_TIME, todoItem.getAlarmTime());

        long id = db.insert(TABLE_TODO, null, values);
        db.close();

        return id;
    }

    // Get all todo items
    public List<TodoItem> getAllTodos() {
        List<TodoItem> todoList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_TODO + " ORDER BY " + COLUMN_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                TodoItem todo = new TodoItem();
                todo.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                todo.setTask(cursor.getString(cursor.getColumnIndex(COLUMN_TASK)));
                todo.setCompleted(cursor.getInt(cursor.getColumnIndex(COLUMN_COMPLETED)) == 1);
                todo.setAlarmTime(cursor.getLong(cursor.getColumnIndex(COLUMN_ALARM_TIME)));

                todoList.add(todo);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return todoList;
    }

    // Update todo item
    public int updateTodo(TodoItem todoItem) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK, todoItem.getTask());
        values.put(COLUMN_COMPLETED, todoItem.isCompleted() ? 1 : 0);
        values.put(COLUMN_ALARM_TIME, todoItem.getAlarmTime());

        int result = db.update(TABLE_TODO, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(todoItem.getId())});

        db.close();
        return result;
    }

    // Delete todo item
    public void deleteTodo(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TODO, COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    // Get todo count
    public int getTodoCount() {
        String countQuery = "SELECT * FROM " + TABLE_TODO;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();
        db.close();

        return count;
    }
}