package com.joeyderuiter.cookieclicker.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(@Nullable Context context) {
        super(context, "cookie_clicker_db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
            "CREATE TABLE highscores (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "highscore INTEGER" +
            ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS highscores");
        onCreate(db);
    }

    public void insertScore(int score) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("highscore", score);

        db.insert("highscores", null, values);
        db.close();
    }

    public int getHighScore() {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT MAX(highscore) FROM highscores";
        Cursor cursor = db.rawQuery(query, null);

        int highScore = 0;

        if (cursor.moveToFirst()) {
            highScore = cursor.getInt(0);
        }

        cursor.close();
        return highScore;
    }
}
