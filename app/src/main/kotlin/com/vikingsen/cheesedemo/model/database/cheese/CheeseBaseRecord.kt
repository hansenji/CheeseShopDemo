/*
 * CheeseBaseRecord.kt
 *
 * GENERATED FILE - DO NOT EDIT
 * CHECKSTYLE:OFF
 * 
 */



package com.vikingsen.cheesedemo.model.database.cheese

import org.dbtools.android.domain.AndroidBaseRecord
import org.dbtools.android.domain.database.statement.StatementWrapper
import org.dbtools.android.domain.database.contentvalues.DBToolsContentValues
import android.database.Cursor


@Suppress("LeakingThis", "unused", "RemoveEmptySecondaryConstructorBody", "ConvertSecondaryConstructorToPrimary")
@SuppressWarnings("all")
abstract class CheeseBaseRecord  : AndroidBaseRecord {

     open var id: Long = 0
     open var name: String = ""
     open var description: String = ""
     open var imageUrl: String = ""
     open var cached: org.threeten.bp.LocalDateTime = org.threeten.bp.LocalDateTime.now()

    constructor() {
    }

    override fun getAllColumns() : Array<String> {
        return CheeseConst.ALL_COLUMNS.clone()
    }

    fun getAllColumnsFull() : Array<String> {
        return CheeseConst.ALL_COLUMNS_FULL.clone()
    }

    override fun getContentValues(values: DBToolsContentValues<*>) {
        values.put(CheeseConst.C_ID, id)
        values.put(CheeseConst.C_NAME, name)
        values.put(CheeseConst.C_DESCRIPTION, description)
        values.put(CheeseConst.C_IMAGE_URL, imageUrl)
        values.put(CheeseConst.C_CACHED, org.dbtools.android.domain.date.DBToolsThreeTenFormatter.localDateTimeToDBString(cached)!!)
    }

    override fun getValues() : Array<Any?> {
        return arrayOf(
            id,
            name,
            description,
            imageUrl,
            org.dbtools.android.domain.date.DBToolsThreeTenFormatter.localDateTimeToDBString(cached)!!)
    }

    open fun copy() : Cheese {
        val copy = Cheese()
        copy.id = id
        copy.name = name
        copy.description = description
        copy.imageUrl = imageUrl
        copy.cached = cached
        return copy
    }

    @Suppress("UNNECESSARY_NOT_NULL_ASSERTION")
    override fun bindInsertStatement(statement: StatementWrapper) {
        statement.bindLong(1, id)
        statement.bindString(2, name)
        statement.bindString(3, description)
        statement.bindString(4, imageUrl)
        statement.bindString(5, org.dbtools.android.domain.date.DBToolsThreeTenFormatter.localDateTimeToDBString(cached)!!)
    }

    @Suppress("UNNECESSARY_NOT_NULL_ASSERTION")
    override fun bindUpdateStatement(statement: StatementWrapper) {
        statement.bindLong(1, id)
        statement.bindString(2, name)
        statement.bindString(3, description)
        statement.bindString(4, imageUrl)
        statement.bindString(5, org.dbtools.android.domain.date.DBToolsThreeTenFormatter.localDateTimeToDBString(cached)!!)
    }

    override fun setContent(values: DBToolsContentValues<*>) {
        id = values.getAsLong(CheeseConst.C_ID)
        name = values.getAsString(CheeseConst.C_NAME)
        description = values.getAsString(CheeseConst.C_DESCRIPTION)
        imageUrl = values.getAsString(CheeseConst.C_IMAGE_URL)
        cached = org.dbtools.android.domain.date.DBToolsThreeTenFormatter.dbStringToLocalDateTime(values.getAsString(CheeseConst.C_CACHED))!!
    }

    override fun setContent(cursor: Cursor) {
        id = cursor.getLong(cursor.getColumnIndexOrThrow(CheeseConst.C_ID))
        name = cursor.getString(cursor.getColumnIndexOrThrow(CheeseConst.C_NAME))
        description = cursor.getString(cursor.getColumnIndexOrThrow(CheeseConst.C_DESCRIPTION))
        imageUrl = cursor.getString(cursor.getColumnIndexOrThrow(CheeseConst.C_IMAGE_URL))
        cached = org.dbtools.android.domain.date.DBToolsThreeTenFormatter.dbStringToLocalDateTime(cursor.getString(cursor.getColumnIndexOrThrow(CheeseConst.C_CACHED)))!!
    }

    override fun isNewRecord() : Boolean {
        return primaryKeyId <= 0
    }

    override fun getIdColumnName() : String {
        return "NO_PRIMARY_KEY"
    }

    override fun getPrimaryKeyId() : Long {
        return 0
    }

    override fun setPrimaryKeyId(id: Long) {
        // NO_PRIMARY_KEY
    }


}