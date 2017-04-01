/*
 * CommentBaseRecord.java
 *
 * GENERATED FILE - DO NOT EDIT
 * CHECKSTYLE:OFF
 * 
 */



package com.vikingsen.cheesedemo.model.database.comment;

import android.database.Cursor;


@SuppressWarnings("all")
public class CommentConst {

    public static final String DATABASE = "main";
    public static final String TABLE = "comment";
    public static final String FULL_TABLE = "main.comment";
    public static final String PRIMARY_KEY_COLUMN = "id";
    public static final String C_ID = "id";
    public static final String FULL_C_ID = "comment.id";
    public static final String C_CHEESE_ID = "cheese_id";
    public static final String FULL_C_CHEESE_ID = "comment.cheese_id";
    public static final String C_USER = "user";
    public static final String FULL_C_USER = "comment.user";
    public static final String C_COMMENT = "comment";
    public static final String FULL_C_COMMENT = "comment.comment";
    public static final String C_CREATED = "created";
    public static final String FULL_C_CREATED = "comment.created";
    public static final String C_UPDATED = "updated";
    public static final String FULL_C_UPDATED = "comment.updated";
    public static final String C_CACHED = "cached";
    public static final String FULL_C_CACHED = "comment.cached";
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS comment (" + 
        "id INTEGER PRIMARY KEY  AUTOINCREMENT," + 
        "cheese_id INTEGER NOT NULL," + 
        "user TEXT NOT NULL," + 
        "comment TEXT NOT NULL," + 
        "created TEXT NOT NULL," + 
        "updated TEXT NOT NULL," + 
        "cached TEXT NOT NULL," + 
        "FOREIGN KEY (cheese_id) REFERENCES cheese (id)" + 
        ");" + 
        "" + 
        "";
    public static final String DROP_TABLE = "DROP TABLE IF EXISTS comment;";
    public static final String INSERT_STATEMENT = "INSERT INTO comment (cheese_id,user,comment,created,updated,cached) VALUES (?,?,?,?,?,?)";
    public static final String UPDATE_STATEMENT = "UPDATE comment SET cheese_id=?, user=?, comment=?, created=?, updated=?, cached=? WHERE id = ?";
    public static final String[] ALL_COLUMNS = new String[] {
        C_ID,
        C_CHEESE_ID,
        C_USER,
        C_COMMENT,
        C_CREATED,
        C_UPDATED,
        C_CACHED};
    public static final String[] ALL_COLUMNS_FULL = new String[] {
        FULL_C_ID,
        FULL_C_CHEESE_ID,
        FULL_C_USER,
        FULL_C_COMMENT,
        FULL_C_CREATED,
        FULL_C_UPDATED,
        FULL_C_CACHED};

    public CommentConst() {
    }

    public static long getId(Cursor cursor) {
        return cursor.getLong(cursor.getColumnIndexOrThrow(C_ID));
    }

    public static long getCheeseId(Cursor cursor) {
        return cursor.getLong(cursor.getColumnIndexOrThrow(C_CHEESE_ID));
    }

    public static String getUser(Cursor cursor) {
        return cursor.getString(cursor.getColumnIndexOrThrow(C_USER));
    }

    public static String getComment(Cursor cursor) {
        return cursor.getString(cursor.getColumnIndexOrThrow(C_COMMENT));
    }

    public static org.threeten.bp.LocalDate getCreated(Cursor cursor) {
        return org.dbtools.android.domain.date.DBToolsThreeTenFormatter.dbStringToLocalDate(cursor.getString(cursor.getColumnIndexOrThrow(C_CREATED)));
    }

    public static org.threeten.bp.LocalDate getUpdated(Cursor cursor) {
        return org.dbtools.android.domain.date.DBToolsThreeTenFormatter.dbStringToLocalDate(cursor.getString(cursor.getColumnIndexOrThrow(C_UPDATED)));
    }

    public static org.threeten.bp.LocalDateTime getCached(Cursor cursor) {
        return org.dbtools.android.domain.date.DBToolsThreeTenFormatter.dbStringToLocalDateTime(cursor.getString(cursor.getColumnIndexOrThrow(C_CACHED)));
    }


}