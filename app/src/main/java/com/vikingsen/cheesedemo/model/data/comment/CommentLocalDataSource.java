package com.vikingsen.cheesedemo.model.data.comment;

import com.vikingsen.cheesedemo.model.database.comment.Comment;
import com.vikingsen.cheesedemo.model.database.comment.CommentManager;
import com.vikingsen.cheesedemo.model.webservice.dto.CommentDto;
import com.vikingsen.cheesedemo.model.webservice.dto.CommentResponse;
import com.vikingsen.cheesedemo.util.SchedulerProvider;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.temporal.ChronoUnit;
import org.threeten.bp.temporal.TemporalUnit;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import timber.log.Timber;

@Singleton
class CommentLocalDataSource {

    private static final long CACHE_VALID_AMOUNT = 1L;
    private static final TemporalUnit CACHE_VALID_UNIT = ChronoUnit.DAYS;

    private final CommentManager commentManager;
    private final SchedulerProvider schedulerProvider;

    @Inject
    CommentLocalDataSource(CommentManager commentManager, SchedulerProvider schedulerProvider) {
        this.commentManager = commentManager;
        this.schedulerProvider = schedulerProvider;
    }

    Single<List<Comment>> getComments(long cheeseId) {
        return commentManager.findAllForCheeseIdRx(cheeseId);
    }

    boolean areCommentsStale(long cheeseId) {
        LocalDateTime cacheExpiration = LocalDateTime.now().minus(CACHE_VALID_AMOUNT, CACHE_VALID_UNIT);
        return commentManager.findOldestCacheData(cheeseId).isBefore(cacheExpiration);
    }

    boolean saveCommentsFromServer(List<CommentDto> commentDtos) {
        commentManager.beginTransaction();
        LocalDateTime cached = LocalDateTime.now();
        boolean commit = false;
        try {
            // TODO delete comments that are no longer valid(on the server)
            for (CommentDto dto : commentDtos) {
                Comment comment = commentManager.findByGuid(dto.getGuid());
                if (comment == null) {
                    comment = new Comment();
                    comment.setGuid(dto.getGuid());
                }
                comment.setCheeseId(dto.getCheeseId());
                comment.setUser(dto.getUser());
                comment.setComment(dto.getComment());
                comment.setCreated(dto.getCreated());
                comment.setSynced(true);
                comment.setCached(cached);
                commentManager.save(comment);
            }
            commit = true;
        } finally {
            commentManager.endTransaction(commit);
        }
        return commit;
    }

    Completable saveNewComment(long cheeseId, String user, String text) {
        return Completable.create(emitter -> {
            try {
                Comment comment = new Comment();
                comment.setGuid(UUID.randomUUID().toString());
                comment.setCheeseId(cheeseId);
                comment.setUser(user);
                comment.setComment(text);
                comment.setCreated(LocalDateTime.now());
                if (commentManager.save(comment)) {
                    emitter.onComplete();
                } else {
                    emitter.onError(new Exception("Failed to save comment " + comment.getGuid()));
                }
            } catch (Exception e) {
                emitter.onError(e);
            }
        }).subscribeOn(schedulerProvider.computation());

    }

    Single<List<Comment>> getNotSyncedComments() {
        return commentManager.findAllNotSyncedRx();
    }

    boolean saveSyncResponses(List<CommentResponse> responses) {
        commentManager.beginTransaction();
        boolean commit = false;
        LocalDateTime cached = LocalDateTime.now();
        try {
            for (CommentResponse response : responses) {
                if (response.isSuccessful()) {
                    commentManager.setCommentSynced(response.getGuid(), cached);
                }
            }
            commit = true;
        } catch (Exception e) {
            Timber.w(e, "Failed to save sync responses");
        } finally {
            commentManager.endTransaction(commit);
        }
        return commit;
    }

    /**
     * Auto subscribes on computation scheduler
     */
    Observable<CommentChange> modelChanges() {
        return commentManager.tableChanges().map(tableChange -> {
            if (tableChange.isBulkOperation()) {
                return CommentChange.bulkOperation();
            }
            return CommentChange.forCheese(commentManager.findCheeseId(tableChange.getRowId()));
        }).subscribeOn(schedulerProvider.computation());
    }
}
