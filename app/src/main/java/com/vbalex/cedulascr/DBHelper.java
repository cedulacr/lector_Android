package com.vbalex.cedulascr;


import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * Created by alex on 2/19/17.
 */



public class DBHelper extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "personas";

    // Contacts table name
    private static final String TABLE_NAME = "personas";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NOMBRE = "name";
    private static final String KEY_APELLIDO1 = "ap1";
    private static final String KEY_APELLIDO2 = "ap2";
    private static final String KEY_GENERO = "g";
    private static final String KEY_F_NACIMIENTO = "fn";
    private static final String KEY_F_VENCIMIENTO = "fv";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " TEXT," +
                KEY_NOMBRE + " TEXT,"+
                KEY_APELLIDO1 + " TEXT,"+
                KEY_APELLIDO2 + " TEXT,"+
                KEY_GENERO + " TEXT,"+
                KEY_F_NACIMIENTO + " TEXT,"+
                KEY_F_VENCIMIENTO + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    void add(Persona p) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, p.getCedula()); // Contact Name
        values.put(KEY_NOMBRE, p.getNombre()); // Contact Phone
        values.put(KEY_APELLIDO1,p.getApellido1());
        values.put(KEY_APELLIDO2,p.getApellido2());
        values.put(KEY_GENERO,""+p.getGenero());
        values.put(KEY_F_NACIMIENTO,p.getFechaNacimiento());
        values.put(KEY_F_VENCIMIENTO,p.getFechaVencimiento());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    Persona get(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[] {
                KEY_ID,
                KEY_NOMBRE,
                KEY_APELLIDO1,
                KEY_APELLIDO2,
                KEY_GENERO,
                KEY_F_NACIMIENTO,
                KEY_F_VENCIMIENTO
        }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Persona p = new Persona(
                cursor.getString(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4).charAt(0),
                cursor.getString(5),
                cursor.getString(6)
        );
        // return contact
        return p;
    }

    public List<Persona> getAll() {
        List<Persona> list = new ArrayList<Persona>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Persona p = new Persona(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4).charAt(0),
                        cursor.getString(5),
                        cursor.getString(6)
                );
                list.add(p);
            } while (cursor.moveToNext());
        }

        return list;
    }

    public void delete(Persona p) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " = ?",
                new String[] { String.valueOf(p.getCedula()) });
        db.close();
    }

    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        return cursor.getCount();
    }

}
