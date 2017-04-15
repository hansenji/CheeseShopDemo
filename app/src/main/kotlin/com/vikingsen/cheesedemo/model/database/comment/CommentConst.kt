/*
 * CommentBaseRecord.kt
 *
 * GENERATED FILE - DO NOT EDIT
 * CHECKSTYLE:OFF
 * 
 */



package com.vikingsen.cheesedemo.model.database.comment

import android.database.Cursor


@Suppress("LeakingThis", "unused", "RemoveEmptySecondaryConstructorBody", "ConvertSecondaryConstructorToPrimary")
@SuppressWarnings("all")
object CommentConst  {

     const val DATABASE = "main"
     const val TABLE = "comment"
     const val FULL_TABLE = "main.comment"
     const val PRIMARY_KEY_COLUMN = "id"
     const val C_ID = "id"
     const val FULL_C_ID = "comment.id"
     const val C_GUID = "guid"
     const val FULL_C_GUID = "comment.guid"
     const val C_CHEESE_ID = "cheese_id"
     const val FULL_C_CHEESE_ID = "comment.cheese_id"
     const val C_USER = "user"
     const val FULL_C_USER = "comment.user"
     const val C_COMMENT = "comment"
     const val FULL_C_COMMENT = "comment.comment"
     const val C_CREATED = "created"
     const val FULL_C_CREATED = "comment.created"
     const val C_SYNCED = "synced"
     const val FULL_C_SYNCED = "comment.synced"
     const val C_CACHED = "cached"
     const val FULL_C_CACHED = "comment.cached"
     const val CREATE_TABLE = "CREATE TABLE IF NOT EXISTS comment (" + 
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
        ""
     const val DROP_TABLE = "DROP TABLE IF EXISTS comment;"
     const val INSERT_STATEMENT = "INSERT INTO comment (guid,cheese_id,user,comment,created,synced,cached) VALUES (?,?,?,?,?,?,?)"
     const val UPDATE_STATEMENT = "UPDATE comment SET guid=?, cheese_id=?, user=?, comment=?, created=?, synced=?, cached=? WHERE id = ?"
     val ALL_COLUMNS = arrayOf(
        C_ID,
        C_GUID,
        C_CHEESE_ID,
        C_USER,
        C_COMMENT,
        C_CREATED,
        C_SYNCED,
        C_CACHED)
     val ALL_COLUMNS_FULL = arrayOf(
        FULL_C_ID,
        FULL_C_GUID,
        FULL_C_CHEESE_ID,
        FULL_C_USER,
        FULL_C_COMMENT,
        FULL_C_CREATED,
        FULL_C_SYNCED,
        FULL_C_CACHED)

    fun getId(cursor: Cursor) : Long {
        return cursor.getLong(cursor.getColumnIndexOrThrow(C_ID))
    }

    fun getGuid(cursor: Cursor) : String {
        return cursor.getString(cursor.getColumnIndexOrThrow(C_GUID))
    }

    fun getCheeseId(cursor: Cursor) : Long {
        return cursor.getLong(cursor.getColumnIndexOrThrow(C_CHEESE_ID))
    }

    fun getUser(cursor: Cursor) : String {
        return cursor.getString(cursor.getColumnIndexOrThrow(C_USER))
    }

    fun getComment(cursor: Cursor) : String {
        return cursor.getString(cursor.getColumnIndexOrThrow(C_COMMENT))
    }

    fun getCreated(cursor: Cursor) : org.threeten.bp.LocalDateTime {
        return org.dbtools.android.domain.date.DBToolsThreeTenFormatter.dbStringToLocalDateTime(cursor.getString(cursor.getColumnIndexOrThrow(C_CREATED)))!!
    }

    fun isSynced(cursor: Cursor) : Boolean {
        return cursor.getInt(cursor.getColumnIndexOrThrow(C_SYNCED)) != 0
    }

    fun getCached(cursor: Cursor) : org.threeten.bp.LocalDateTime? {
        return org.dbtools.android.domain.date.DBToolsThreeTenFormatter.dbStringToLocalDateTime(cursor.getString(cursor.getColumnIndexOrThrow(C_CACHED)))
    }


}