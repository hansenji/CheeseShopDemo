package com.vikingsen.cheesedemo.job;

import android.support.annotation.NonNull;

import com.evernote.android.job.Job;
import com.vikingsen.cheesedemo.model.data.comment.CommentRepository;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import timber.log.Timber;


class CommentSyncJob extends Job {

    static final String TAG = "CommentSyncJob";
    static final long EXECUTION_WINDOW_START = TimeUnit.SECONDS.toMillis(1);
    static final long EXECUTION_WINDOW_END = TimeUnit.SECONDS.toMillis(5);
    static final long BACK_OFF_MS = TimeUnit.SECONDS.toMillis(1);

    private final CommentRepository commentRepository;

    @Inject
    CommentSyncJob(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @NonNull
    @Override
    protected Result onRunJob(Params params) {
        try {
            if (commentRepository.syncComments()) {
                return Result.SUCCESS;
            } else {
                return Result.RESCHEDULE;
            }
        } catch (Exception e) {
            Timber.e("Failed to post comments");
            return Result.RESCHEDULE;
        }
    }
}
