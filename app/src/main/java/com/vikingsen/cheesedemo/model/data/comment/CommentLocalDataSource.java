package com.vikingsen.cheesedemo.model.data.comment;

import com.vikingsen.cheesedemo.model.database.comment.Comment;
import com.vikingsen.cheesedemo.model.database.comment.CommentManager;
import com.vikingsen.cheesedemo.model.webservice.dto.CommentDto;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.temporal.ChronoUnit;
import org.threeten.bp.temporal.TemporalUnit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import hu.akarnokd.rxjava.interop.RxJavaInterop;
import io.reactivex.Single;

@Singleton
class CommentLocalDataSource {

    private static final long CACHE_VALID_AMOUNT = 1L;
    private static final TemporalUnit CACHE_VALID_UNIT = ChronoUnit.DAYS;

    private final CommentManager commentManager;

    @Inject
    CommentLocalDataSource(CommentManager commentManager) {
        this.commentManager = commentManager;
    }

    Single<List<Comment>> getComments(long cheeseId) {
        return RxJavaInterop.toV2Observable(commentManager.findAllForCheeseIdRx(cheeseId))
                .single(Collections.emptyList());
    }

    boolean areCommentsStale(long cheeseId) {
        LocalDateTime cacheExpiration = LocalDateTime.now().minus(CACHE_VALID_AMOUNT, CACHE_VALID_UNIT);
        return commentManager.findOldestCacheData(cheeseId).isBefore(cacheExpiration);
    }

    List<Comment> saveComments(List<CommentDto> commentDtos) {
        commentManager.beginTransaction();
        List<Comment> comments = new ArrayList<>(commentDtos.size());
        LocalDateTime cached = LocalDateTime.now();
        boolean commit = false;
        try {
            for (CommentDto dto : commentDtos) {
                Comment comment = commentManager.findByCheeseIdAndUser(dto.getCheeseId(), dto.getUser());
                if (comment == null) {
                    comment = new Comment();
                    comment.setCheeseId(dto.getCheeseId());
                    comment.setUser(dto.getUser());
                }
                comment.setComment(dto.getComment());
                comment.setCreated(dto.getCreated());
                comment.setUpdated(dto.getUpdated());
                comment.setCached(cached);
                commentManager.save(comment);
                comments.add(comment);
            }
            commit = true;
        } finally {
            commentManager.endTransaction(commit);
        }
        return comments;
    }
}
