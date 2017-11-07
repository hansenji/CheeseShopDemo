/*
 * CommentBaseManager.kt
 *
 * GENERATED FILE - DO NOT EDIT
 * 
 */



package com.vikingsen.cheesedemo.model.database.comment

import com.vikingsen.cheesedemo.model.database.DatabaseManager
import org.dbtools.android.domain.RxKotlinAndroidBaseManagerWritable


@Suppress("unused")
@SuppressWarnings("all")
abstract class CommentBaseManager (databaseManager: DatabaseManager) : RxKotlinAndroidBaseManagerWritable<Comment>(databaseManager) {

    override val allColumns: Array<String> = CommentConst.ALL_COLUMNS
    override val primaryKey = CommentConst.PRIMARY_KEY_COLUMN
    override val dropSql = CommentConst.DROP_TABLE
    override val createSql = CommentConst.CREATE_TABLE
    override val insertSql = CommentConst.INSERT_STATEMENT
    override val updateSql = CommentConst.UPDATE_STATEMENT

    override fun getDatabaseName() : String {
        return CommentConst.DATABASE
    }

    override fun newRecord() : Comment {
        return Comment()
    }

    override fun getTableName() : String {
        return CommentConst.TABLE
    }


}