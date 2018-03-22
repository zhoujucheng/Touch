package com.dnnt.touch.base

import io.reactivex.Scheduler
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.ExecutorService
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by dnnt on 17-12-23.
 */

@Singleton
class MyScheduler @Inject constructor(executorService: ExecutorService): Scheduler() {

    private val mExecutorService = executorService

    override fun createWorker(): Worker {
        return ExecutorScheduler.ExecutorWorker(mExecutorService)
    }
}
