package com.vikingsen.cheesedemo.job

import com.evernote.android.job.Job
import com.vikingsen.cheesedemo.model.data.comment.CommentRepository
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class CommentSyncJob
@Inject constructor(private val commentRepository: CommentRepository) : Job() {

    override fun onRunJob(params: Job.Params): Job.Result {
        try {
            if (commentRepository.syncComments()) {
                return Job.Result.SUCCESS
            } else {
                return Job.Result.RESCHEDULE
            }
        } catch (e: Exception) {
            Timber.e("Failed to post comments")
            return Job.Result.RESCHEDULE
        }

    }

    companion object {

        internal val TAG = "CommentSyncJob"
        internal val EXECUTION_WINDOW_START = TimeUnit.SECONDS.toMillis(1)
        internal val EXECUTION_WINDOW_END = TimeUnit.SECONDS.toMillis(5)
        internal val BACK_OFF_MS = TimeUnit.SECONDS.toMillis(1)
    }
}
