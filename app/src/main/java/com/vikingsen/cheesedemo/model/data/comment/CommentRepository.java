package com.vikingsen.cheesedemo.model.data.comment;


import com.vikingsen.cheesedemo.model.database.comment.Comment;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;
import io.reactivex.SingleSource;
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
                    Single<List<Comment>> remoteComments = getAndSaveRemoteComments(id);
                    Single<List<Comment>> localComments = localDataSource.getComments(id);
                    if (force || localDataSource.areCommentsStale(id)) {
                        Timber.d("COMMENTS ARE STALE");
                        return concatSources(remoteComments, localComments);
                    } else {
                        Timber.d("COMMENTS ARE FRESH");
                        return concatSources(localComments, remoteComments);
                    }
                });
    }

    private Single<List<Comment>> getAndSaveRemoteComments(long cheeseId) {
        return remoteDataSource.getComments(cheeseId).map(comments -> localDataSource.saveComments(comments));
    }

    private SingleSource<List<Comment>> concatSources(Single<List<Comment>> source1, Single<List<Comment>> source2) {
        return Single.concat(source1, source2)
                .filter(list -> !list.isEmpty())
                .first(Collections.emptyList());
    }
}
