package com.vikingsen.cheesedemo.job


import com.evernote.android.job.JobRequest

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppJobScheduler
@Inject constructor() {

    fun scheduleCommentSync() {
        JobRequest.Builder(CommentSyncJob.TAG)
                .setUpdateCurrent(true)
                .setExecutionWindow(CommentSyncJob.EXECUTION_WINDOW_START, CommentSyncJob.EXECUTION_WINDOW_END)
                .setBackoffCriteria(CommentSyncJob.BACK_OFF_MS, JobRequest.BackoffPolicy.EXPONENTIAL)
                .setRequiredNetworkType(JobRequest.NetworkType.CONNECTED)
                .setRequirementsEnforced(true)
                .build()
                .schedule()
    }

}
