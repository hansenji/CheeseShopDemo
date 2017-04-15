/*
 * CheeseManager.kt
 *
 * Generated on: 04/14/2017 02:45:41
 *
 */



package com.vikingsen.cheesedemo.model.database.cheese

import com.vikingsen.cheesedemo.model.database.DatabaseManager
import io.reactivex.Maybe
import io.reactivex.Single
import org.dbtools.android.domain.date.DBToolsThreeTenFormatter
import org.dbtools.query.sql.SQLQueryBuilder
import org.threeten.bp.LocalDateTime
import javax.inject.Inject


@javax.inject.Singleton
class CheeseManager
@Inject constructor(databaseManager: DatabaseManager) : CheeseBaseManager(databaseManager) {

    fun findByCheeseIdRx(cheeseId: Long): Maybe<Cheese> {
        return findBySelectionRx(
                selection = "${CheeseConst.C_ID} = ?",
                selectionArgs = SQLQueryBuilder.toSelectionArgs(cheeseId)
        )
    }

    fun findOldestCacheDate(): LocalDateTime {
        val cached = DBToolsThreeTenFormatter.dbStringToLocalDateTime(
                findValueBySelection(
                        valueType = String::class.java,
                        column = CheeseConst.C_CACHED,
                        defaultValue = null,
                        orderBy = "${CheeseConst.C_CACHED} DESC"
                )
        ) ?: return DAWN_OF_TIME
        return cached
    }

    fun findCacheDate(id: Long): LocalDateTime {
        val cached = DBToolsThreeTenFormatter.dbStringToLocalDateTime(
                findValueBySelection(
                        valueType = String::class.java,
                        column = CheeseConst.C_CACHED,
                        defaultValue = null,
                        selection = "${CheeseConst.C_ID} = ?",
                        selectionArgs = SQLQueryBuilder.toSelectionArgs(id)
                )
        ) ?: return DAWN_OF_TIME
        return cached
    }

    fun findAllCheesesRx(): Single<List<Cheese>> {
        // GOTCHA - MATCH ORDERING FROM SERVER
        return findAllRx(orderBy = CheeseConst.C_NAME)
    }

    companion object {
        private val DAWN_OF_TIME = LocalDateTime.of(1970, 1, 1, 0, 0)
    }
}