/*
 * CheeseBaseManager.java
 *
 * GENERATED FILE - DO NOT EDIT
 * 
 */



package com.vikingsen.cheesedemo.model.database.cheese;

import com.vikingsen.cheesedemo.model.database.DatabaseManager;
import org.dbtools.android.domain.RxAndroidBaseManagerWritable;


@SuppressWarnings("all")
public abstract class CheeseBaseManager extends RxAndroidBaseManagerWritable<Cheese> {


    public CheeseBaseManager(DatabaseManager databaseManager) {
        super(databaseManager);
    }

    @javax.annotation.Nonnull
    public String getDatabaseName() {
        return CheeseConst.DATABASE;
    }

    @javax.annotation.Nonnull
    public Cheese newRecord() {
        return new Cheese();
    }

    @javax.annotation.Nonnull
    public String getTableName() {
        return CheeseConst.TABLE;
    }

    @javax.annotation.Nonnull
    public String[] getAllColumns() {
        return CheeseConst.ALL_COLUMNS;
    }

    @javax.annotation.Nonnull
    public String getPrimaryKey() {
        return CheeseConst.PRIMARY_KEY_COLUMN;
    }

    @javax.annotation.Nonnull
    public String getDropSql() {
        return CheeseConst.DROP_TABLE;
    }

    @javax.annotation.Nonnull
    public String getCreateSql() {
        return CheeseConst.CREATE_TABLE;
    }

    @javax.annotation.Nonnull
    public String getInsertSql() {
        return CheeseConst.INSERT_STATEMENT;
    }

    @javax.annotation.Nonnull
    public String getUpdateSql() {
        return CheeseConst.UPDATE_STATEMENT;
    }


}