package com.vikingsen.cheesedemo.model.data.comment


import android.support.annotation.WorkerThread
import com.vikingsen.cheesedemo.job.AppJobScheduler
import com.vikingsen.cheesedemo.model.database.comment.Comment
import com.vikingsen.cheesedemo.model.webservice.dto.CommentRequestDto
import io.reactivex.Observable
import io.reactivex.Single
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommentRepository @Inject
internal constructor(private val remoteDataSource: CommentRemoteDataSource, private val localDataSource: CommentLocalDataSource, private val appJobScheduler: AppJobScheduler) {

    fun getComments(cheeseId: Long, forceRefresh: Boolean): Single<List<Comment>> {
        return Single.just(Pair(cheeseId, forceRefresh))
                .flatMap<List<Comment>> {
                    val id = it.first
                    val force = it.second

                    val localComments = localDataSource.getComments(id)

                    if (force || localDataSource.areCommentsStale(id)) {
                        Timber.d("COMMENTS ARE STALE")
                        return@flatMap getAndSaveRemoteComments(id)

                                // Always load from the database to display not yet synced comments
                                // We will be notified through modelChanges to load from the database
                                .flatMap { _ -> localComments }

                    } else {
                        Timber.d("COMMENTS ARE FRESH")
                        return@flatMap localComments
                    }
                }
    }

    fun addComment(cheeseId: Long, user: String, comment: String) {
        localDataSource.saveNewComment(cheeseId, user, comment)
                .subscribe(
                        { appJobScheduler.scheduleCommentSync() },
                        { e -> Timber.e(e, "Failed to save comment") }
                )
    }

    @WorkerThread
    fun syncComments(): Boolean {
        return localDataSource.getNotSyncedComments()
                .toObservable()
                .flatMapIterable { it }
                .map { CommentRequestDto(it.id, it.cheeseId, it.user, it.comment) }
                .toList()
                .flatMapMaybe { remoteDataSource.syncComments(it) }
                .map { localDataSource.saveSyncResponses(it) }
                .blockingGet(false)
    }

    /**
     * Auto subscribes on computation scheduler
     */
    fun modelChanges(): Observable<CommentChange> {
        return localDataSource.modelChanges()
    }

    private fun getAndSaveRemoteComments(cheeseId: Long): Single<Boolean> {
        return remoteDataSource.getComments(cheeseId)
                .map { localDataSource.saveCommentsFromServer(it) }
                .onErrorReturn {
                    Timber.e(it, "Failed to fetch comments for cheese: %d", cheeseId)
                    return@onErrorReturn false
                }
    }
}
