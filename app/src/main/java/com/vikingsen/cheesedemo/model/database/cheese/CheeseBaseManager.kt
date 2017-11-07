/*
 * CheeseBaseManager.kt
 *
 * GENERATED FILE - DO NOT EDIT
 * 
 */



package com.vikingsen.cheesedemo.model.database.cheese

import com.vikingsen.cheesedemo.model.database.DatabaseManager
import org.dbtools.android.domain.RxKotlinAndroidBaseManagerWritable


@Suppress("unused")
@SuppressWarnings("all")
abstract class CheeseBaseManager (databaseManager: DatabaseManager) : RxKotlinAndroidBaseManagerWritable<Cheese>(databaseManager) {

    override val allColumns: Array<String> = CheeseConst.ALL_COLUMNS
    override val primaryKey = "NO_PRIMARY_KEY"
    override val dropSql = CheeseConst.DROP_TABLE
    override val createSql = CheeseConst.CREATE_TABLE
    override val insertSql = CheeseConst.INSERT_STATEMENT
    override val updateSql = CheeseConst.UPDATE_STATEMENT

    override fun getDatabaseName() : String {
        return CheeseConst.DATABASE
    }

    override fun newRecord() : Cheese {
        return Cheese()
    }

    override fun getTableName() : String {
        return CheeseConst.TABLE
    }


}