package com.vikingsen.cheesedemo.model.work

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.vikingsen.cheesedemo.inject.Injector
import com.vikingsen.cheesedemo.model.repository.comment.CommentRepository
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkScheduler
@Inject constructor(
    private val workManager: WorkManager
) {

    fun scheduleCommentSync() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val workRequest = OneTimeWorkRequestBuilder<SyncWorker>()
            .setInitialDelay(1, TimeUnit.SECONDS)
            .setConstraints(constraints)
            .build()
        workManager.enqueueUniqueWork(SyncWorker.UNIQUE_NAME, ExistingWorkPolicy.REPLACE, workRequest)
    }
}

class SyncWorker(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {

    @Inject
    lateinit var commentRepository: CommentRepository

    init {
        Injector.get().inject(this)
    }

    override suspend fun doWork(): Result {
        try {
            if (commentRepository.syncComments()) {
                return Result.success()
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to post comments")
        }
        // TODO: 4/7/19 consider using counts to stop after so many retries
        return Result.retry()
    }

    companion object {
        const val UNIQUE_NAME = "SYNC_COMMENT_UNIQUE"
    }
}
