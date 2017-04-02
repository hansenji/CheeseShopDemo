/*
 * CheeseBaseRecord.java
 *
 * GENERATED FILE - DO NOT EDIT
 * CHECKSTYLE:OFF
 * 
 */



package com.vikingsen.cheesedemo.model.database.cheese;

import org.dbtools.android.domain.AndroidBaseRecord;
import org.dbtools.android.domain.database.statement.StatementWrapper;
import org.dbtools.android.domain.database.contentvalues.DBToolsContentValues;
import android.database.Cursor;


@SuppressWarnings("all")
public abstract class CheeseBaseRecord extends AndroidBaseRecord {

    private long id = 0;
    private String name = "";
    private String description = "";
    private String imageUrl = "";
    private org.threeten.bp.LocalDateTime cached = org.threeten.bp.LocalDateTime.now();

    public CheeseBaseRecord() {
    }

    @Override
    public String[] getAllColumns() {
        return CheeseConst.ALL_COLUMNS.clone();
    }

    public String[] getAllColumnsFull() {
        return CheeseConst.ALL_COLUMNS_FULL.clone();
    }

    @Override
    public void getContentValues(DBToolsContentValues values) {
        values.put(CheeseConst.C_ID, id);
        values.put(CheeseConst.C_NAME, name);
        values.put(CheeseConst.C_DESCRIPTION, description);
        values.put(CheeseConst.C_IMAGE_URL, imageUrl);
        values.put(CheeseConst.C_CACHED, org.dbtools.android.domain.date.DBToolsThreeTenFormatter.localDateTimeToDBString(cached));
    }

    @Override
    public Object[] getValues() {
        Object[] values = new Object[]{
            id,
            name,
            description,
            imageUrl,
            org.dbtools.android.domain.date.DBToolsThreeTenFormatter.localDateTimeToDBString(cached),
        };
        return values;
    }

    public Cheese copy() {
        Cheese copy = new Cheese();
        copy.setId(id);
        copy.setName(name);
        copy.setDescription(description);
        copy.setImageUrl(imageUrl);
        copy.setCached(cached);
        return copy;
    }

    @Override
    public void bindInsertStatement(StatementWrapper statement) {
        statement.bindLong(1, id);
        statement.bindString(2, name);
        statement.bindString(3, description);
        statement.bindString(4, imageUrl);
        statement.bindString(5, org.dbtools.android.domain.date.DBToolsThreeTenFormatter.localDateTimeToDBString(cached));
    }

    @Override
    public void bindUpdateStatement(StatementWrapper statement) {
        statement.bindLong(1, id);
        statement.bindString(2, name);
        statement.bindString(3, description);
        statement.bindString(4, imageUrl);
        statement.bindString(5, org.dbtools.android.domain.date.DBToolsThreeTenFormatter.localDateTimeToDBString(cached));
    }

    public void setContent(DBToolsContentValues values) {
        id = values.getAsLong(CheeseConst.C_ID);
        name = values.getAsString(CheeseConst.C_NAME);
        description = values.getAsString(CheeseConst.C_DESCRIPTION);
        imageUrl = values.getAsString(CheeseConst.C_IMAGE_URL);
        cached = org.dbtools.android.domain.date.DBToolsThreeTenFormatter.dbStringToLocalDateTime(values.getAsString(CheeseConst.C_CACHED));
    }

    @Override
    public void setContent(Cursor cursor) {
        id = cursor.getLong(cursor.getColumnIndexOrThrow(CheeseConst.C_ID));
        name = cursor.getString(cursor.getColumnIndexOrThrow(CheeseConst.C_NAME));
        description = cursor.getString(cursor.getColumnIndexOrThrow(CheeseConst.C_DESCRIPTION));
        imageUrl = cursor.getString(cursor.getColumnIndexOrThrow(CheeseConst.C_IMAGE_URL));
        cached = org.dbtools.android.domain.date.DBToolsThreeTenFormatter.dbStringToLocalDateTime(cursor.getString(cursor.getColumnIndexOrThrow(CheeseConst.C_CACHED)));
    }

    public boolean isNewRecord() {
        return getPrimaryKeyId() <= 0;
    }

    @Override
    public String getIdColumnName() {
        return "NO_PRIMARY_KEY";
    }

    @Override
    public long getPrimaryKeyId() {
        return 0;
    }

    @Override
    public void setPrimaryKeyId(long id) {
        // NO_PRIMARY_KEY
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @javax.annotation.Nonnull
    public String getName() {
        return name;
    }

    public void setName(@javax.annotation.Nonnull String name) {
        this.name = name;
    }

    @javax.annotation.Nonnull
    public String getDescription() {
        return description;
    }

    public void setDescription(@javax.annotation.Nonnull String description) {
        this.description = description;
    }

    @javax.annotation.Nonnull
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(@javax.annotation.Nonnull String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @javax.annotation.Nonnull
    public org.threeten.bp.LocalDateTime getCached() {
        return cached;
    }

    public void setCached(@javax.annotation.Nonnull org.threeten.bp.LocalDateTime cached) {
        this.cached = cached;
    }


}