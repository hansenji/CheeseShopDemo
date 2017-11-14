package com.vikingsen.cheesedemo.job

import com.evernote.android.job.Job
import com.evernote.android.job.JobCreator
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class AppJobCreator
@Inject constructor(private val commentSyncJobProvider: Provider<CommentSyncJob>) : JobCreator {

    override fun create(tag: String): Job? = when (tag) {
        CommentSyncJob.TAG -> commentSyncJobProvider.get()
        else -> {
            Timber.w("Cannot find job for tag [%s]. Be sure to add Job to AppJobCreator", tag)
            null
        }
    }
}
