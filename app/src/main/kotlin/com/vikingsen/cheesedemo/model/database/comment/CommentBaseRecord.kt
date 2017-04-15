/*
 * CommentBaseRecord.kt
 *
 * GENERATED FILE - DO NOT EDIT
 * CHECKSTYLE:OFF
 * 
 */



package com.vikingsen.cheesedemo.model.database.comment

import org.dbtools.android.domain.AndroidBaseRecord
import org.dbtools.android.domain.database.statement.StatementWrapper
import org.dbtools.android.domain.database.contentvalues.DBToolsContentValues
import android.database.Cursor


@Suppress("LeakingThis", "unused", "RemoveEmptySecondaryConstructorBody", "ConvertSecondaryConstructorToPrimary")
@SuppressWarnings("all")
abstract class CommentBaseRecord  : AndroidBaseRecord {

     open var id: Long = 0
     open var guid: String = ""
     open var cheeseId: Long = 0
     open var user: String = ""
     open var comment: String = ""
     open var created: org.threeten.bp.LocalDateTime = org.threeten.bp.LocalDateTime.now()
     open var synced: Boolean = false
     open var cached: org.threeten.bp.LocalDateTime? = null

    constructor() {
    }

    override fun getIdColumnName() : String {
        return CommentConst.C_ID
    }

    override fun getPrimaryKeyId() : Long {
        return id
    }

    override fun setPrimaryKeyId(id: Long) {
        this.id = id
    }

    override fun getAllColumns() : Array<String> {
        return CommentConst.ALL_COLUMNS.clone()
    }

    fun getAllColumnsFull() : Array<String> {
        return CommentConst.ALL_COLUMNS_FULL.clone()
    }

    override fun getContentValues(values: DBToolsContentValues<*>) {
        values.put(CommentConst.C_GUID, guid)
        values.put(CommentConst.C_CHEESE_ID, cheeseId)
        values.put(CommentConst.C_USER, user)
        values.put(CommentConst.C_COMMENT, comment)
        values.put(CommentConst.C_CREATED, org.dbtools.android.domain.date.DBToolsThreeTenFormatter.localDateTimeToDBString(created)!!)
        values.put(CommentConst.C_SYNCED, if (synced) 1L else 0L)
        values.put(CommentConst.C_CACHED, org.dbtools.android.domain.date.DBToolsThreeTenFormatter.localDateTimeToDBString(cached))
    }

    override fun getValues() : Array<Any?> {
        return arrayOf(
            id,
            guid,
            cheeseId,
            user,
            comment,
            org.dbtools.android.domain.date.DBToolsThreeTenFormatter.localDateTimeToDBString(created)!!,
            if (synced) 1L else 0L,
            org.dbtools.android.domain.date.DBToolsThreeTenFormatter.localDateTimeToDBString(cached))
    }

    open fun copy() : Comment {
        val copy = Comment()
        copy.id = id
        copy.guid = guid
        copy.cheeseId = cheeseId
        copy.user = user
        copy.comment = comment
        copy.created = created
        copy.synced = synced
        copy.cached = cached
        return copy
    }

    @Suppress("UNNECESSARY_NOT_NULL_ASSERTION")
    override fun bindInsertStatement(statement: StatementWrapper) {
        statement.bindString(1, guid)
        statement.bindLong(2, cheeseId)
        statement.bindString(3, user)
        statement.bindString(4, comment)
        statement.bindString(5, org.dbtools.android.domain.date.DBToolsThreeTenFormatter.localDateTimeToDBString(created)!!)
        statement.bindLong(6, if (synced) 1L else 0L)
        if (cached != null) {
            statement.bindString(7, org.dbtools.android.domain.date.DBToolsThreeTenFormatter.localDateTimeToDBString(cached)!!)
        } else {
            statement.bindNull(7)
        }
    }

    @Suppress("UNNECESSARY_NOT_NULL_ASSERTION")
    override fun bindUpdateStatement(statement: StatementWrapper) {
        statement.bindString(1, guid)
        statement.bindLong(2, cheeseId)
        statement.bindString(3, user)
        statement.bindString(4, comment)
        statement.bindString(5, org.dbtools.android.domain.date.DBToolsThreeTenFormatter.localDateTimeToDBString(created)!!)
        statement.bindLong(6, if (synced) 1L else 0L)
        if (cached != null) {
            statement.bindString(7, org.dbtools.android.domain.date.DBToolsThreeTenFormatter.localDateTimeToDBString(cached)!!)
        } else {
            statement.bindNull(7)
        }
        statement.bindLong(8, id)
    }

    override fun setContent(values: DBToolsContentValues<*>) {
        guid = values.getAsString(CommentConst.C_GUID)
        cheeseId = values.getAsLong(CommentConst.C_CHEESE_ID)
        user = values.getAsString(CommentConst.C_USER)
        comment = values.getAsString(CommentConst.C_COMMENT)
        created = org.dbtools.android.domain.date.DBToolsThreeTenFormatter.dbStringToLocalDateTime(values.getAsString(CommentConst.C_CREATED))!!
        synced = values.getAsBoolean(CommentConst.C_SYNCED)
        cached = org.dbtools.android.domain.date.DBToolsThreeTenFormatter.dbStringToLocalDateTime(values.getAsString(CommentConst.C_CACHED))
    }

    override fun setContent(cursor: Cursor) {
        id = cursor.getLong(cursor.getColumnIndexOrThrow(CommentConst.C_ID))
        guid = cursor.getString(cursor.getColumnIndexOrThrow(CommentConst.C_GUID))
        cheeseId = cursor.getLong(cursor.getColumnIndexOrThrow(CommentConst.C_CHEESE_ID))
        user = cursor.getString(cursor.getColumnIndexOrThrow(CommentConst.C_USER))
        comment = cursor.getString(cursor.getColumnIndexOrThrow(CommentConst.C_COMMENT))
        created = org.dbtools.android.domain.date.DBToolsThreeTenFormatter.dbStringToLocalDateTime(cursor.getString(cursor.getColumnIndexOrThrow(CommentConst.C_CREATED)))!!
        synced = cursor.getInt(cursor.getColumnIndexOrThrow(CommentConst.C_SYNCED)) != 0
        cached = org.dbtools.android.domain.date.DBToolsThreeTenFormatter.dbStringToLocalDateTime(cursor.getString(cursor.getColumnIndexOrThrow(CommentConst.C_CACHED)))
    }

    override fun isNewRecord() : Boolean {
        return primaryKeyId <= 0
    }


}