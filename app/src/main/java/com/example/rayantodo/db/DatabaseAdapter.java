package com.example.rayantodo.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseAdapter extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "todo";
    private static final int DATABASE_VERSION = 1;


    public static final String TASK_ID = "id";
    private static final String TASK_TABLE = "task";
    public static final String TASK_TITLE = "title";
    public static final String TASK_DESCRIPTION = "description";
    public static final String TASK_STATUS = "status";


    private static final String CREATE_TASK_TABLE = "CREATE TABLE " + TASK_TABLE + "(" + TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TASK_TITLE + " TEXT, "
            + TASK_DESCRIPTION + " TEXT, "
            + TASK_STATUS + " INTEGER)";


    public DatabaseAdapter(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private SQLiteDatabase db;


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TASK_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TASK_TABLE);
        onCreate(sqLiteDatabase);
    }


    public void openDatabase() {
        db = this.getWritableDatabase();
    }

    public void closeDatabase() {
        db.close();
    }


    public void insertTask(TodoModel task) {
        ContentValues cv = new ContentValues();
        cv.put(TASK_TITLE, task.getTitle());
        cv.put(TASK_DESCRIPTION, task.getDescription());
        cv.put(TASK_STATUS, task.getStatus());
        db.insert(TASK_TABLE, null, cv);
    }


    public void updateTask(int id, String title, String description) {
        ContentValues cv = new ContentValues();
        cv.put(TASK_TITLE, title);
        cv.put(TASK_DESCRIPTION, description);
        db.update(TASK_TABLE, cv, TASK_ID + " = ?", new String[]{String.valueOf(id)});

    }

    public void deleteTask(int id) {
        db.delete(TASK_TABLE, TASK_ID + " = ?", new String[]{String.valueOf(id)});
    }


    public void updateTaskStatus(int id, int status) {
        ContentValues cv = new ContentValues();
        cv.put(TASK_STATUS, status);
        db.update(TASK_TABLE, cv, TASK_ID + " = ?", new String[]{String.valueOf(id)});
    }


    public List<TodoModel> getAllTasks() {
        List<TodoModel> taskList = new ArrayList<>();
        db.beginTransaction();

        try (Cursor cursor = db.query(TASK_TABLE, null, null, null, null, null, null)) {
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    int idIndex = cursor.getColumnIndex(TASK_ID);
                    int titleIndex = cursor.getColumnIndex(TASK_TITLE);
                    int descriptionIndex = cursor.getColumnIndex(TASK_DESCRIPTION);
                    int statusIndex = cursor.getColumnIndex(TASK_STATUS);
                    do {
                        TodoModel task = new TodoModel();
                        task.setId(cursor.getInt(idIndex));
                        task.setTitle(cursor.getString(titleIndex));
                        task.setDescription(cursor.getString(descriptionIndex));
                        task.setStatus(cursor.getInt(statusIndex));
                        taskList.add(task);
                    } while (cursor.moveToNext());
                }
            }
        } finally {
            db.endTransaction();
        }
        return taskList;
    }

}

