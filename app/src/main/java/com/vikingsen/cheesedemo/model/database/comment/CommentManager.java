/*
 * CommentManager.java
 *
 * Generated on: 04/01/2017 09:45:31
 *
 */



package com.vikingsen.cheesedemo.model.database.comment;

import com.vikingsen.cheesedemo.model.database.DatabaseManager;

import org.dbtools.android.domain.date.DBToolsThreeTenFormatter;
import org.dbtools.query.sql.SQLQueryBuilder;
import org.threeten.bp.LocalDateTime;

import java.util.List;

import rx.Observable;


@javax.inject.Singleton
public class CommentManager extends CommentBaseManager {

    private static final LocalDateTime DAWN_OF_TIME = LocalDateTime.of(1970, 1, 1, 0, 0);

    @javax.inject.Inject
    public CommentManager(DatabaseManager databaseManager) {
        super(databaseManager);
    }

    public Observable<List<Comment>> findAllForCheeseIdRx(long cheeseId) {
        return findAllBySelectionRx(CommentConst.C_CHEESE_ID + "=?", SQLQueryBuilder.toSelectionArgs(cheeseId),
                CommentConst.C_UPDATED + " DESC, " + CommentConst.C_CREATED + " DESC");
    }

    public LocalDateTime findOldestCacheData(long cheeseId) {
        LocalDateTime cached = DBToolsThreeTenFormatter.dbStringToLocalDateTime(
                findValueBySelection(String.class, CommentConst.C_CACHED, CommentConst.C_CHEESE_ID + "=?", SQLQueryBuilder.toSelectionArgs(cheeseId),
                        CommentConst.C_CACHED + " DESC", null)
        );
        if (cached == null) {
            return DAWN_OF_TIME;
        }
        return cached;
    }

    public Comment findByCheeseIdAndUser(long cheeseId, String user) {
        return findBySelection(CommentConst.C_CHEESE_ID + "=? AND " + CommentConst.C_USER + "=?", SQLQueryBuilder.toSelectionArgs(cheeseId, user), null);
    }
}