package com.vikingsen.cheesedemo.model.data.comment;

import com.vikingsen.cheesedemo.model.database.comment.Comment;
import com.vikingsen.cheesedemo.model.database.comment.CommentManager;
import com.vikingsen.cheesedemo.model.webservice.dto.CommentDto;
import com.vikingsen.cheesedemo.util.SchedulerProvider;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.temporal.ChronoUnit;
import org.threeten.bp.temporal.TemporalUnit;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Singleton;

import hu.akarnokd.rxjava.interop.RxJavaInterop;
import io.reactivex.Observable;
import io.reactivex.Single;

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
        return RxJavaInterop.toV2Observable(commentManager.findAllForCheeseIdRx(cheeseId))
                .single(Collections.emptyList());
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

    void saveNewComment(long cheeseId, String user, String text) {
        Single.<Boolean>create(emitter -> {
            try {
                Comment comment = new Comment();
                comment.setGuid(UUID.randomUUID().toString());
                comment.setCheeseId(cheeseId);
                comment.setUser(user);
                comment.setComment(text);
                comment.setCreated(LocalDate.now());
                emitter.onSuccess(commentManager.save(comment));
            } catch (Exception e) {
                emitter.onError(e);
            }
        }).subscribeOn(schedulerProvider.computation())
                .subscribe(saved -> {
                    if (saved) {
                        // TODO schedule post
                    }
                });

    }

    /**
     * Auto subscribes on computation scheduler
     */
    Observable<CommentChange> modelChanges() {
        return RxJavaInterop.toV2Observable(commentManager.tableChanges()).map(tableChange -> {
            if (tableChange.isBulkOperation()) {
                return CommentChange.bulkOperation();
            }
            return CommentChange.forCheese(commentManager.findCheeseId(tableChange.getRowId()));
        }).subscribeOn(schedulerProvider.computation());
    }
}
