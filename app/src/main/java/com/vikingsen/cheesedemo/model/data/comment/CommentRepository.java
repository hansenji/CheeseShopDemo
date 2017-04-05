package com.vikingsen.cheesedemo.model.data.comment;


import com.vikingsen.cheesedemo.model.database.comment.Comment;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.Single;
import kotlin.Pair;
import timber.log.Timber;

@Singleton
public class CommentRepository {

    private final CommentRemoteDataSource remoteDataSource;
    private final CommentLocalDataSource localDataSource;

    @Inject
    CommentRepository(CommentRemoteDataSource remoteDataSource, CommentLocalDataSource localDataSource) {
        this.remoteDataSource = remoteDataSource;
        this.localDataSource = localDataSource;
    }

    public Single<List<Comment>> getComments(long cheeseId, boolean forceRefresh) {
        return Single.just(new Pair<>(cheeseId, forceRefresh))
                .flatMap(pair -> {
                    long id = pair.getFirst();
                    boolean force = pair.getSecond();
                    if (force || localDataSource.areCommentsStale(id)) {
                        Timber.d("COMMENTS ARE STALE");
                        return getAndSaveRemoteComments(id)

                                // Always load from the database to display not yet synced comments
                                // We will be notified through modelChanges to load from the database
                                .map(success -> Collections.emptyList());

                    } else {
                        Timber.d("COMMENTS ARE FRESH");
                        return localDataSource.getComments(id);
                    }
                });
    }

    public void addComment(long cheeseId, String user, String comment) {
        localDataSource.saveNewComment(cheeseId, user, comment);
    }

    /**
     * Auto subscribes on computation scheduler
     */
    public Observable<CommentChange> modelChanges() {
        return localDataSource.modelChanges();
    }

    private Single<Boolean> getAndSaveRemoteComments(long cheeseId) {
        return remoteDataSource.getComments(cheeseId)
                .map(comments -> localDataSource.saveCommentsFromServer(comments))
                .onErrorReturn(throwable -> {
                    Timber.e(throwable, "Failed to fetch comments for cheese: %d", cheeseId);
                    return false;
                });
    }
}
