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
    public static final String C_GUID = "guid";
    public static final String FULL_C_GUID = "comment.guid";
    public static final String C_CHEESE_ID = "cheese_id";
    public static final String FULL_C_CHEESE_ID = "comment.cheese_id";
    public static final String C_USER = "user";
    public static final String FULL_C_USER = "comment.user";
    public static final String C_COMMENT = "comment";
    public static final String FULL_C_COMMENT = "comment.comment";
    public static final String C_CREATED = "created";
    public static final String FULL_C_CREATED = "comment.created";
    public static final String C_SYNCED = "synced";
    public static final String FULL_C_SYNCED = "comment.synced";
    public static final String C_CACHED = "cached";
    public static final String FULL_C_CACHED = "comment.cached";
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS comment (" + 
        "id INTEGER PRIMARY KEY  AUTOINCREMENT," + 
        "guid TEXT NOT NULL," + 
        "cheese_id INTEGER NOT NULL," + 
        "user TEXT NOT NULL," + 
        "comment TEXT NOT NULL," + 
        "created TEXT NOT NULL," + 
        "synced INTEGER DEFAULT 0 NOT NULL," + 
        "cached TEXT," + 
        "UNIQUE(guid)," + 
        "FOREIGN KEY (cheese_id) REFERENCES cheese (id)" + 
        ");" + 
        "" + 
        "CREATE INDEX IF NOT EXISTS commentguid_IDX ON comment (guid);" + 
        "" + 
        "";
    public static final String DROP_TABLE = "DROP TABLE IF EXISTS comment;";
    public static final String INSERT_STATEMENT = "INSERT INTO comment (guid,cheese_id,user,comment,created,synced,cached) VALUES (?,?,?,?,?,?,?)";
    public static final String UPDATE_STATEMENT = "UPDATE comment SET guid=?, cheese_id=?, user=?, comment=?, created=?, synced=?, cached=? WHERE id = ?";
    public static final String[] ALL_COLUMNS = new String[] {
        C_ID,
        C_GUID,
        C_CHEESE_ID,
        C_USER,
        C_COMMENT,
        C_CREATED,
        C_SYNCED,
        C_CACHED};
    public static final String[] ALL_COLUMNS_FULL = new String[] {
        FULL_C_ID,
        FULL_C_GUID,
        FULL_C_CHEESE_ID,
        FULL_C_USER,
        FULL_C_COMMENT,
        FULL_C_CREATED,
        FULL_C_SYNCED,
        FULL_C_CACHED};

    public CommentConst() {
    }

    public static long getId(Cursor cursor) {
        return cursor.getLong(cursor.getColumnIndexOrThrow(C_ID));
    }

    public static String getGuid(Cursor cursor) {
        return cursor.getString(cursor.getColumnIndexOrThrow(C_GUID));
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

    public static boolean isSynced(Cursor cursor) {
        return cursor.getInt(cursor.getColumnIndexOrThrow(C_SYNCED)) != 0 ? true : false;
    }

    public static org.threeten.bp.LocalDateTime getCached(Cursor cursor) {
        return org.dbtools.android.domain.date.DBToolsThreeTenFormatter.dbStringToLocalDateTime(cursor.getString(cursor.getColumnIndexOrThrow(C_CACHED)));
    }


}