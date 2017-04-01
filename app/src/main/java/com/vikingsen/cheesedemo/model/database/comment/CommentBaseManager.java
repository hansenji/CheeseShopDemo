/*
 * CommentBaseManager.java
 *
 * GENERATED FILE - DO NOT EDIT
 * 
 */



package com.vikingsen.cheesedemo.model.database.comment;

import com.vikingsen.cheesedemo.model.database.DatabaseManager;
import org.dbtools.android.domain.RxAndroidBaseManagerWritable;


@SuppressWarnings("all")
public abstract class CommentBaseManager extends RxAndroidBaseManagerWritable<Comment> {


    public CommentBaseManager(DatabaseManager databaseManager) {
        super(databaseManager);
    }

    @javax.annotation.Nonnull
    public String getDatabaseName() {
        return CommentConst.DATABASE;
    }

    @javax.annotation.Nonnull
    public Comment newRecord() {
        return new Comment();
    }

    @javax.annotation.Nonnull
    public String getTableName() {
        return CommentConst.TABLE;
    }

    @javax.annotation.Nonnull
    public String[] getAllColumns() {
        return CommentConst.ALL_COLUMNS;
    }

    @javax.annotation.Nonnull
    public String getPrimaryKey() {
        return CommentConst.PRIMARY_KEY_COLUMN;
    }

    @javax.annotation.Nonnull
    public String getDropSql() {
        return CommentConst.DROP_TABLE;
    }

    @javax.annotation.Nonnull
    public String getCreateSql() {
        return CommentConst.CREATE_TABLE;
    }

    @javax.annotation.Nonnull
    public String getInsertSql() {
        return CommentConst.INSERT_STATEMENT;
    }

    @javax.annotation.Nonnull
    public String getUpdateSql() {
        return CommentConst.UPDATE_STATEMENT;
    }


}