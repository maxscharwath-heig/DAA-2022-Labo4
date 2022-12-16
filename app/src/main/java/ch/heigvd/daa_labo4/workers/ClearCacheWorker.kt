package ch.heigvd.daa_labo4.workers

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import ch.heigvd.daa_labo4.Cache

class ClearCacheWorker(appContext: Context, workerParams: WorkerParameters): Worker(appContext, workerParams) {
    override fun doWork(): Result {
        Cache.clear()
        return Result.success();
    }
}