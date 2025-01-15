package com.example.tarea7_listatareas;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class BaseDatos extends SQLiteOpenHelper {

    private static final String nombreDb = "Base De Datos";
    private static final int versionDb = 1;

    public BaseDatos(@Nullable Context context) {
        super(context, nombreDb, null, versionDb);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("CREATE TABLE  tareas (id integer primary key autoincrement, asignatura text, descripcion text, fecha text, hora text, estado integer)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS  tareas");
        onCreate(sqLiteDatabase);

    }
}
