package com.example.java_oglen.urunyonetimi;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DB extends SQLiteOpenHelper {

    private static String dbName = "urunTanitim.db";
    private static int dbVersion = 1;

    public DB(Context context) {
        super(context, dbName, null, dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE `favoriler` (\n" +
                "\t`fid`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "\t`uid`\tTEXT UNIQUE,\n" +
                "\t`kid`\tTEXT,\n" +
                "\t`ubaslik`\tTEXT,\n" +
                "\t`ufiyat`\tTEXT\n" +
                ");";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS favoriler");
        onCreate(db);
    }
}
