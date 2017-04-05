/*
 * CheeseBaseRecord.java
 *
 * GENERATED FILE - DO NOT EDIT
 * CHECKSTYLE:OFF
 * 
 */



package com.vikingsen.cheesedemo.model.database.cheese;

import android.database.Cursor;


@SuppressWarnings("all")
public class CheeseConst {

    public static final String DATABASE = "main";
    public static final String TABLE = "cheese";
    public static final String FULL_TABLE = "main.cheese";
    public static final String C_ID = "id";
    public static final String FULL_C_ID = "cheese.id";
    public static final String C_NAME = "name";
    public static final String FULL_C_NAME = "cheese.name";
    public static final String C_DESCRIPTION = "description";
    public static final String FULL_C_DESCRIPTION = "cheese.description";
    public static final String C_IMAGE_URL = "image_url";
    public static final String FULL_C_IMAGE_URL = "cheese.image_url";
    public static final String C_CACHED = "cached";
    public static final String FULL_C_CACHED = "cheese.cached";
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS cheese (" + 
        "id INTEGER NOT NULL," + 
        "name TEXT NOT NULL," + 
        "description TEXT NOT NULL," + 
        "image_url TEXT NOT NULL," + 
        "cached TEXT NOT NULL," + 
        "UNIQUE(id) ON CONFLICT REPLACE" + 
        ");" + 
        "" + 
        "CREATE INDEX IF NOT EXISTS cheeseid_IDX ON cheese (id);" + 
        "" + 
        "";
    public static final String DROP_TABLE = "DROP TABLE IF EXISTS cheese;";
    public static final String INSERT_STATEMENT = "INSERT INTO cheese (id,name,description,image_url,cached) VALUES (?,?,?,?,?)";
    public static final String UPDATE_STATEMENT = "UPDATE cheese SET id=?, name=?, description=?, image_url=?, cached=? WHERE  = ?";
    public static final String[] ALL_COLUMNS = new String[] {
        C_ID,
        C_NAME,
        C_DESCRIPTION,
        C_IMAGE_URL,
        C_CACHED};
    public static final String[] ALL_COLUMNS_FULL = new String[] {
        FULL_C_ID,
        FULL_C_NAME,
        FULL_C_DESCRIPTION,
        FULL_C_IMAGE_URL,
        FULL_C_CACHED};

    public CheeseConst() {
    }

    public static long getId(Cursor cursor) {
        return cursor.getLong(cursor.getColumnIndexOrThrow(C_ID));
    }

    public static String getName(Cursor cursor) {
        return cursor.getString(cursor.getColumnIndexOrThrow(C_NAME));
    }

    public static String getDescription(Cursor cursor) {
        return cursor.getString(cursor.getColumnIndexOrThrow(C_DESCRIPTION));
    }

    public static String getImageUrl(Cursor cursor) {
        return cursor.getString(cursor.getColumnIndexOrThrow(C_IMAGE_URL));
    }

    public static org.threeten.bp.LocalDateTime getCached(Cursor cursor) {
        return org.dbtools.android.domain.date.DBToolsThreeTenFormatter.dbStringToLocalDateTime(cursor.getString(cursor.getColumnIndexOrThrow(C_CACHED)));
    }


}