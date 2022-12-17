package ch.heigvd.daa_labo4.workers

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import ch.heigvd.daa_labo4.Cache

/**
 * Worker for deleting files in the cache directory
 *
 * @author Nicolas Crausaz
 * @author Lazar Pavicevic
 * @author Maxime Scharwath
 */
class ClearCacheWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    companion object {
        private val TAG = ClearCacheWorker::class.qualifiedName
    }

    override fun doWork(): Result {
        Log.d(TAG, "Cleared cache dir")
        Cache.clear()
        return Result.success()
    }
}