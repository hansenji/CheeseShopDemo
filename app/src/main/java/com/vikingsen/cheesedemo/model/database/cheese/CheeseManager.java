/*
 * CheeseManager.java
 *
 * Generated on: 04/01/2017 09:45:31
 *
 */


package com.vikingsen.cheesedemo.model.database.cheese;

import com.vikingsen.cheesedemo.model.database.DatabaseManager;

import org.dbtools.android.domain.date.DBToolsThreeTenFormatter;
import org.dbtools.query.sql.SQLQueryBuilder;
import org.threeten.bp.LocalDateTime;

import java.util.List;

import rx.Observable;


@javax.inject.Singleton
public class CheeseManager extends CheeseBaseManager {

    private static final LocalDateTime DAWN_OF_TIME = LocalDateTime.of(1970, 1, 1, 0, 0);

    @javax.inject.Inject
    public CheeseManager(DatabaseManager databaseManager) {
        super(databaseManager);
    }

    public Cheese findByCheeseId(long cheeseId) {
        return findBySelection(CheeseConst.C_ID + "=?", SQLQueryBuilder.toSelectionArgs(cheeseId), null);
    }

    public LocalDateTime findOldestCacheDate() {
        LocalDateTime cached = DBToolsThreeTenFormatter.dbStringToLocalDateTime(
                findValueBySelection(String.class, CheeseConst.C_CACHED, null, null, CheeseConst.C_CACHED + " DESC", null)
        );
        if (cached == null) {
            return DAWN_OF_TIME;
        }
        return cached;
    }

    public LocalDateTime findCacheDate(long id) {
        LocalDateTime cached = DBToolsThreeTenFormatter.dbStringToLocalDateTime(
                findValueBySelection(String.class, CheeseConst.C_CACHED, CheeseConst.C_ID + "=?", SQLQueryBuilder.toSelectionArgs(id), null)
        );
        if (cached == null) {
            return DAWN_OF_TIME;
        }
        return cached;
    }

    public Observable<List<Cheese>> findAllCheesesRx() {
        return findAllOrderByRx(CheeseConst.C_NAME);
    }
}