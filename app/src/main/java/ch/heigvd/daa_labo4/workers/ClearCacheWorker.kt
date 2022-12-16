package ch.heigvd.daa_labo4.workers

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import ch.heigvd.daa_labo4.utils.Cache

class ClearCacheWorker(appContext: Context, workerParams: WorkerParameters): Worker(appContext, workerParams) {
    override fun doWork(): Result {
        Log.d("ClearCacheWorker", "Cleared cache dir")
        Cache.clear()
        return Result.success()
    }
}