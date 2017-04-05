package com.vikingsen.cheesedemo.job;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import timber.log.Timber;

@Singleton
public class AppJobCreator implements JobCreator {

    private final Provider<CommentSyncJob> commentSyncJobProvider;

    @Inject
    AppJobCreator(Provider<CommentSyncJob> commentSyncJobProvider) {
        this.commentSyncJobProvider = commentSyncJobProvider;
    }

    @Override
    public Job create(String tag) {
        switch (tag) {
            case CommentSyncJob.TAG:
                return commentSyncJobProvider.get();
            default:
                Timber.w("Cannot find job for tag [%s]. Be sure to add Job to AppJobCreator", tag);
                return null;
        }
    }
}
