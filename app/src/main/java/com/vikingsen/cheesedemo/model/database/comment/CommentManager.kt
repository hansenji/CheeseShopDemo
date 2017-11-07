/*
 * CommentManager.kt
 *
 * Generated on: 04/14/2017 02:45:41
 *
 */



package com.vikingsen.cheesedemo.model.database.comment

import com.vikingsen.cheesedemo.model.database.DatabaseManager
import io.reactivex.Single
import org.dbtools.android.domain.date.DBToolsThreeTenFormatter
import org.dbtools.query.sql.SQLQueryBuilder
import org.threeten.bp.LocalDateTime
import javax.inject.Inject


@javax.inject.Singleton
class CommentManager
@Inject constructor(databaseManager: DatabaseManager) : CommentBaseManager(databaseManager) {

    fun findAllForCheeseIdRx(cheeseId: Long): Single<List<Comment>> {
        return findAllBySelectionRx(
                selection = "${CommentConst.C_CHEESE_ID} = ?",
                selectionArgs = SQLQueryBuilder.toSelectionArgs(cheeseId),
                orderBy = "${CommentConst.C_CREATED} DESC")
    }

    fun findOldestCacheData(cheeseId: Long): LocalDateTime {
        val cached = DBToolsThreeTenFormatter.dbStringToLocalDateTime(
                findValueBySelection(
                        valueType = String::class.java,
                        column = "MIN(${CommentConst.C_CACHED})",
                        defaultValue = null,
                        selection = "${CommentConst.C_CHEESE_ID} = ?",
                        selectionArgs = SQLQueryBuilder.toSelectionArgs(cheeseId)
                )
        ) ?: return DAWN_OF_TIME
        return cached
    }

    fun findByGuid(guid: String): Comment? {
        return findBySelection(
                selection = CommentConst.C_GUID + "=?",
                selectionArgs = SQLQueryBuilder.toSelectionArgs(guid)
        )
    }

    fun findCheeseId(id: Long): Long {
        return findValueByRowId(
                valueType = Long::class.java,
                column = CommentConst.C_CHEESE_ID,
                rowId = id,
                defaultValue = -1L
        )
    }

    fun findAllNotSyncedRx(): Single<List<Comment>> {
        return findAllBySelectionRx(selection = "${CommentConst.C_SYNCED} = 0")
    }

    fun setCommentSynced(guid: String, cached: LocalDateTime) {
        val contentValues = createNewDBToolsContentValues().apply {
            put(CommentConst.C_SYNCED, true)
            put(CommentConst.C_CACHED, DBToolsThreeTenFormatter.localDateTimeToDBString(cached))
        }

        update(contentValues, "${CommentConst.C_GUID} = ?", SQLQueryBuilder.toSelectionArgs(guid))
    }

    companion object {
        private val DAWN_OF_TIME = LocalDateTime.of(1970, 1, 1, 0, 0)
    }

}