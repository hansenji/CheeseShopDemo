/*
 * CommentManager.java
 *
 * Generated on: 04/01/2017 09:45:31
 *
 */



package com.vikingsen.cheesedemo.model.database.comment;

import com.vikingsen.cheesedemo.model.database.DatabaseManager;

import org.dbtools.android.domain.database.contentvalues.DBToolsContentValues;
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
                CommentConst.C_CREATED + " DESC");
    }

    public LocalDateTime findOldestCacheData(long cheeseId) {
        LocalDateTime cached = DBToolsThreeTenFormatter.dbStringToLocalDateTime(
                findValueBySelection(String.class, "MIN(" + CommentConst.C_CACHED + ")", CommentConst.C_CHEESE_ID + "=?",
                        SQLQueryBuilder.toSelectionArgs(cheeseId), null)
        );
        if (cached == null) {
            return DAWN_OF_TIME;
        }
        return cached;
    }

    public Comment findByGuid(String guid) {
        return findBySelection(CommentConst.C_GUID + "=?", SQLQueryBuilder.toSelectionArgs(guid), null);
    }

    public long findCheeseId(long id) {
        return findValueBySelection(Long.class, CommentConst.C_CHEESE_ID, id, -1L);
    }

    public Observable<List<Comment>> findAllNotSyncedRx() {
        return findAllBySelectionRx(CommentConst.C_SYNCED + "=0", null, null);
    }

    public void setCommentSynced(String guid, LocalDateTime cached) {
        DBToolsContentValues contentValues = createNewDBToolsContentValues();
        contentValues.put(CommentConst.C_SYNCED, true);
        contentValues.put(CommentConst.C_CACHED, DBToolsThreeTenFormatter.localDateTimeToDBString(cached));
        update(contentValues, CommentConst.C_GUID + "=?", SQLQueryBuilder.toSelectionArgs(guid));
    }
}