/*
 * CommentBaseRecord.java
 *
 * GENERATED FILE - DO NOT EDIT
 * CHECKSTYLE:OFF
 * 
 */



package com.vikingsen.cheesedemo.model.database.comment;

import org.dbtools.android.domain.AndroidBaseRecord;
import org.dbtools.android.domain.database.statement.StatementWrapper;
import org.dbtools.android.domain.database.contentvalues.DBToolsContentValues;
import android.database.Cursor;


@SuppressWarnings("all")
public abstract class CommentBaseRecord extends AndroidBaseRecord {

    private long id = 0;
    private long cheeseId = 0;
    private String user = "";
    private String comment = "";
    private org.threeten.bp.LocalDate created = org.threeten.bp.LocalDate.now();
    private org.threeten.bp.LocalDate updated = org.threeten.bp.LocalDate.now();
    private org.threeten.bp.LocalDateTime cached = org.threeten.bp.LocalDateTime.now();

    public CommentBaseRecord() {
    }

    @Override
    public String getIdColumnName() {
        return CommentConst.C_ID;
    }

    @Override
    public long getPrimaryKeyId() {
        return id;
    }

    @Override
    public void setPrimaryKeyId(long id) {
        this.id = id;
    }

    @Override
    public String[] getAllColumns() {
        return CommentConst.ALL_COLUMNS.clone();
    }

    public String[] getAllColumnsFull() {
        return CommentConst.ALL_COLUMNS_FULL.clone();
    }

    @Override
    public void getContentValues(DBToolsContentValues values) {
        values.put(CommentConst.C_CHEESE_ID, cheeseId);
        values.put(CommentConst.C_USER, user);
        values.put(CommentConst.C_COMMENT, comment);
        values.put(CommentConst.C_CREATED, org.dbtools.android.domain.date.DBToolsThreeTenFormatter.localDateToDBString(created));
        values.put(CommentConst.C_UPDATED, org.dbtools.android.domain.date.DBToolsThreeTenFormatter.localDateToDBString(updated));
        values.put(CommentConst.C_CACHED, org.dbtools.android.domain.date.DBToolsThreeTenFormatter.localDateTimeToDBString(cached));
    }

    @Override
    public Object[] getValues() {
        Object[] values = new Object[]{
            id,
            cheeseId,
            user,
            comment,
            org.dbtools.android.domain.date.DBToolsThreeTenFormatter.localDateToDBString(created),
            org.dbtools.android.domain.date.DBToolsThreeTenFormatter.localDateToDBString(updated),
            org.dbtools.android.domain.date.DBToolsThreeTenFormatter.localDateTimeToDBString(cached),
        };
        return values;
    }

    public Comment copy() {
        Comment copy = new Comment();
        copy.setId(id);
        copy.setCheeseId(cheeseId);
        copy.setUser(user);
        copy.setComment(comment);
        copy.setCreated(created);
        copy.setUpdated(updated);
        copy.setCached(cached);
        return copy;
    }

    @Override
    public void bindInsertStatement(StatementWrapper statement) {
        statement.bindLong(1, cheeseId);
        statement.bindString(2, user);
        statement.bindString(3, comment);
        statement.bindString(4, org.dbtools.android.domain.date.DBToolsThreeTenFormatter.localDateToDBString(created));
        statement.bindString(5, org.dbtools.android.domain.date.DBToolsThreeTenFormatter.localDateToDBString(updated));
        statement.bindString(6, org.dbtools.android.domain.date.DBToolsThreeTenFormatter.localDateTimeToDBString(cached));
    }

    @Override
    public void bindUpdateStatement(StatementWrapper statement) {
        statement.bindLong(1, cheeseId);
        statement.bindString(2, user);
        statement.bindString(3, comment);
        statement.bindString(4, org.dbtools.android.domain.date.DBToolsThreeTenFormatter.localDateToDBString(created));
        statement.bindString(5, org.dbtools.android.domain.date.DBToolsThreeTenFormatter.localDateToDBString(updated));
        statement.bindString(6, org.dbtools.android.domain.date.DBToolsThreeTenFormatter.localDateTimeToDBString(cached));
        statement.bindLong(7, id);
    }

    public void setContent(DBToolsContentValues values) {
        cheeseId = values.getAsLong(CommentConst.C_CHEESE_ID);
        user = values.getAsString(CommentConst.C_USER);
        comment = values.getAsString(CommentConst.C_COMMENT);
        created = org.dbtools.android.domain.date.DBToolsThreeTenFormatter.dbStringToLocalDate(values.getAsString(CommentConst.C_CREATED));
        updated = org.dbtools.android.domain.date.DBToolsThreeTenFormatter.dbStringToLocalDate(values.getAsString(CommentConst.C_UPDATED));
        cached = org.dbtools.android.domain.date.DBToolsThreeTenFormatter.dbStringToLocalDateTime(values.getAsString(CommentConst.C_CACHED));
    }

    @Override
    public void setContent(Cursor cursor) {
        id = cursor.getLong(cursor.getColumnIndexOrThrow(CommentConst.C_ID));
        cheeseId = cursor.getLong(cursor.getColumnIndexOrThrow(CommentConst.C_CHEESE_ID));
        user = cursor.getString(cursor.getColumnIndexOrThrow(CommentConst.C_USER));
        comment = cursor.getString(cursor.getColumnIndexOrThrow(CommentConst.C_COMMENT));
        created = org.dbtools.android.domain.date.DBToolsThreeTenFormatter.dbStringToLocalDate(cursor.getString(cursor.getColumnIndexOrThrow(CommentConst.C_CREATED)));
        updated = org.dbtools.android.domain.date.DBToolsThreeTenFormatter.dbStringToLocalDate(cursor.getString(cursor.getColumnIndexOrThrow(CommentConst.C_UPDATED)));
        cached = org.dbtools.android.domain.date.DBToolsThreeTenFormatter.dbStringToLocalDateTime(cursor.getString(cursor.getColumnIndexOrThrow(CommentConst.C_CACHED)));
    }

    public boolean isNewRecord() {
        return getPrimaryKeyId() <= 0;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCheeseId() {
        return cheeseId;
    }

    public void setCheeseId(long cheeseId) {
        this.cheeseId = cheeseId;
    }

    @javax.annotation.Nonnull
    public String getUser() {
        return user;
    }

    public void setUser(@javax.annotation.Nonnull String user) {
        this.user = user;
    }

    @javax.annotation.Nonnull
    public String getComment() {
        return comment;
    }

    public void setComment(@javax.annotation.Nonnull String comment) {
        this.comment = comment;
    }

    @javax.annotation.Nonnull
    public org.threeten.bp.LocalDate getCreated() {
        return created;
    }

    public void setCreated(@javax.annotation.Nonnull org.threeten.bp.LocalDate created) {
        this.created = created;
    }

    @javax.annotation.Nonnull
    public org.threeten.bp.LocalDate getUpdated() {
        return updated;
    }

    public void setUpdated(@javax.annotation.Nonnull org.threeten.bp.LocalDate updated) {
        this.updated = updated;
    }

    @javax.annotation.Nonnull
    public org.threeten.bp.LocalDateTime getCached() {
        return cached;
    }

    public void setCached(@javax.annotation.Nonnull org.threeten.bp.LocalDateTime cached) {
        this.cached = cached;
    }


}