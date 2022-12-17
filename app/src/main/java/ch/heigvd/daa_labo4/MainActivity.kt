package ch.heigvd.daa_labo4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.lifecycleScope
import androidx.work.*
import ch.heigvd.daa_labo4.workers.ClearCacheWorker
import java.util.concurrent.TimeUnit
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.WorkRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import java.net.URL

/**
 * Main activity of the application
 *
 * @author Nicolas Crausaz
 * @author Lazar Pavicevic
 * @author Maxime Scharwath
 */
class MainActivity : AppCompatActivity() {

    private val clearCacheRequest: WorkRequest =
        OneTimeWorkRequestBuilder<ClearCacheWorker>()
            .build()

    private lateinit var clearCachePeriodicRequest: WorkRequest

    companion object {
        const val CLEAR_CACHE_INTERVAL = 15L
        const val PICTURES_NB = 10000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Init the cache object default path
        Cache.setDir(cacheDir)

        // Generate list of URLs
        val items = List(PICTURES_NB) {
            val num = it + 1
            URL("https://daa.iict.ch/images/$num.jpg")
        }

        val recycler = findViewById<RecyclerView>(R.id.recycler)
        val adapter = ImageRecyclerAdapter(items, lifecycleScope)

        recycler.adapter = adapter
        recycler.layoutManager = GridLayoutManager(this, 3)

        // Bind the periodic cache clear
        clearCachePeriodicRequest =
            PeriodicWorkRequestBuilder<ClearCacheWorker>(
                CLEAR_CACHE_INTERVAL,
                TimeUnit.MINUTES
            ).build()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_actions_refresh -> {
                launchClearCache()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onStop() {
        super.onStop()
        lifecycleScope.coroutineContext.cancelChildren()
    }

    private fun launchClearCache() {
        WorkManager
            .getInstance(applicationContext)
            .enqueue(clearCacheRequest)
    }
}