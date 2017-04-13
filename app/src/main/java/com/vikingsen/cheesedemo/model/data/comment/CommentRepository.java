package com.vikingsen.cheesedemo.model.data.comment;


import android.support.annotation.WorkerThread;

import com.vikingsen.cheesedemo.job.AppJobScheduler;
import com.vikingsen.cheesedemo.model.database.comment.Comment;
import com.vikingsen.cheesedemo.model.webservice.dto.CommentRequestDto;

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
    private final AppJobScheduler appJobScheduler;

    @Inject
    CommentRepository(CommentRemoteDataSource remoteDataSource, CommentLocalDataSource localDataSource, AppJobScheduler appJobScheduler) {
        this.remoteDataSource = remoteDataSource;
        this.localDataSource = localDataSource;
        this.appJobScheduler = appJobScheduler;
    }

    public Single<List<Comment>> getComments(long cheeseId, boolean forceRefresh) {
        return Single.just(new Pair<>(cheeseId, forceRefresh))
                .flatMap(pair -> {
                    long id = pair.getFirst();
                    boolean force = pair.getSecond();

                    Single<List<Comment>> localComments = localDataSource.getComments(id);

                    if (force || localDataSource.areCommentsStale(id)) {
                        Timber.d("COMMENTS ARE STALE");
                        return getAndSaveRemoteComments(id)

                                // Always load from the database to display not yet synced comments
                                // We will be notified through modelChanges to load from the database
                                .flatMap(success -> localComments);

                    } else {
                        Timber.d("COMMENTS ARE FRESH");
                        return localComments;
                    }
                });
    }

    public void addComment(long cheeseId, String user, String comment) {
        localDataSource.saveNewComment(cheeseId, user, comment)
                .subscribe(
                        () -> appJobScheduler.scheduleCommentSync(),
                        e -> Timber.e(e, "Failed to save comment")
                );
    }

    @WorkerThread
    public boolean syncComments() {
        return localDataSource.getNotSyncedComments()
                .toObservable()
                .flatMapIterable(list -> list)
                .map(comment -> new CommentRequestDto(comment.getGuid(), comment.getCheeseId(), comment.getUser(), comment.getComment()))
                .toList()
                .flatMapMaybe(requestDtos -> remoteDataSource.syncComments(requestDtos))
                .map(responses -> localDataSource.saveSyncResponses(responses))
                .blockingGet(false);
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
