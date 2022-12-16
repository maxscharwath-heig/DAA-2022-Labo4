package ch.heigvd.daa_labo4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.lifecycle.lifecycleScope
import androidx.work.*
import ch.heigvd.daa_labo4.workers.ClearCacheWorker
import java.util.concurrent.TimeUnit
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.WorkRequest
import ch.heigvd.daa_labo4.utils.Cache
import ch.heigvd.daa_labo4.utils.ImageDownloader
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val clearCacheRequest: WorkRequest =
        OneTimeWorkRequestBuilder<ClearCacheWorker>()
            .build()

    private lateinit var clearCachePeriodicRequest: WorkRequest

    //private val imageRetriever = ImageRetriever(lifecycleScope, cacheDir)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Cache.setDir(cacheDir)

        val items = listOf(1..10000).flatten() // change to string

        val recycler = findViewById<RecyclerView>(R.id.recycler)
        val adapter = ImageRecyclerAdapter(items, lifecycleScope)

        recycler.adapter = adapter
        recycler.layoutManager = GridLayoutManager(this, 3)

        clearCachePeriodicRequest =
            PeriodicWorkRequestBuilder<ClearCacheWorker>(15, TimeUnit.MINUTES).build()
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

    private fun launchClearCache() {
        WorkManager
            .getInstance(applicationContext)
            .enqueue(clearCacheRequest)
    }
}