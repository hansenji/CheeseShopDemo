/*
 * DatabaseBaseManager.kt
 *
 * GENERATED FILE - DO NOT EDIT
 * CHECKSTYLE:OFF
 * 
 */



package com.vikingsen.cheesedemo.model.database

import org.dbtools.android.domain.AndroidDatabase
import org.dbtools.android.domain.AndroidBaseManager
import org.dbtools.android.domain.AndroidDatabaseManager
import org.dbtools.android.domain.config.DatabaseConfig


@Suppress("unused", "ConvertSecondaryConstructorToPrimary", "RemoveEmptySecondaryConstructorBody")
@SuppressWarnings("all")
abstract class DatabaseBaseManager  : AndroidDatabaseManager {


    constructor(databaseConfig: DatabaseConfig) : super(databaseConfig) {
    }

    open fun createMainTables(androidDatabase: AndroidDatabase) {
        val database = androidDatabase.databaseWrapper
        database.beginTransaction()
        
        // Enum Tables
        
        // Tables
        AndroidBaseManager.createTable(database, com.vikingsen.cheesedemo.model.database.cheese.CheeseConst.CREATE_TABLE)
        AndroidBaseManager.createTable(database, com.vikingsen.cheesedemo.model.database.comment.CommentConst.CREATE_TABLE)
        
        database.setTransactionSuccessful()
        database.endTransaction()
    }

    override fun onCreate(androidDatabase: AndroidDatabase) {
        logger.i(TAG, "Creating database: ${androidDatabase.name}")
        if (androidDatabase.name == DatabaseManagerConst.MAIN_DATABASE_NAME) {
            createMainTables(androidDatabase)
        }
    }

    override fun onCreateViews(androidDatabase: AndroidDatabase) {
        logger.i(TAG, "Creating database views: ${androidDatabase.name}")
    }

    override fun onDropViews(androidDatabase: AndroidDatabase) {
        logger.i(TAG, "Dropping database views: ${androidDatabase.name}")
    }


}