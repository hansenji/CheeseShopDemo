/*
 * CheeseBaseRecord.kt
 *
 * GENERATED FILE - DO NOT EDIT
 * CHECKSTYLE:OFF
 * 
 */



package com.vikingsen.cheesedemo.model.database.cheese

import android.database.Cursor


@Suppress("LeakingThis", "unused", "RemoveEmptySecondaryConstructorBody", "ConvertSecondaryConstructorToPrimary")
@SuppressWarnings("all")
object CheeseConst  {

     const val DATABASE = "main"
     const val TABLE = "cheese"
     const val FULL_TABLE = "main.cheese"
     const val C_ID = "id"
     const val FULL_C_ID = "cheese.id"
     const val C_NAME = "name"
     const val FULL_C_NAME = "cheese.name"
     const val C_DESCRIPTION = "description"
     const val FULL_C_DESCRIPTION = "cheese.description"
     const val C_IMAGE_URL = "image_url"
     const val FULL_C_IMAGE_URL = "cheese.image_url"
     const val C_CACHED = "cached"
     const val FULL_C_CACHED = "cheese.cached"
     const val CREATE_TABLE = "CREATE TABLE IF NOT EXISTS cheese (" + 
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
        ""
     const val DROP_TABLE = "DROP TABLE IF EXISTS cheese;"
     const val INSERT_STATEMENT = "INSERT INTO cheese (id,name,description,image_url,cached) VALUES (?,?,?,?,?)"
     const val UPDATE_STATEMENT = "UPDATE cheese SET id=?, name=?, description=?, image_url=?, cached=? WHERE  = ?"
     val ALL_COLUMNS = arrayOf(
        C_ID,
        C_NAME,
        C_DESCRIPTION,
        C_IMAGE_URL,
        C_CACHED)
     val ALL_COLUMNS_FULL = arrayOf(
        FULL_C_ID,
        FULL_C_NAME,
        FULL_C_DESCRIPTION,
        FULL_C_IMAGE_URL,
        FULL_C_CACHED)

    fun getId(cursor: Cursor) : Long {
        return cursor.getLong(cursor.getColumnIndexOrThrow(C_ID))
    }

    fun getName(cursor: Cursor) : String {
        return cursor.getString(cursor.getColumnIndexOrThrow(C_NAME))
    }

    fun getDescription(cursor: Cursor) : String {
        return cursor.getString(cursor.getColumnIndexOrThrow(C_DESCRIPTION))
    }

    fun getImageUrl(cursor: Cursor) : String {
        return cursor.getString(cursor.getColumnIndexOrThrow(C_IMAGE_URL))
    }

    fun getCached(cursor: Cursor) : org.threeten.bp.LocalDateTime {
        return org.dbtools.android.domain.date.DBToolsThreeTenFormatter.dbStringToLocalDateTime(cursor.getString(cursor.getColumnIndexOrThrow(C_CACHED)))!!
    }


}