package ch.heigvd.daa_labo4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.work.*
import ch.heigvd.daa_labo4.workers.ClearCacheWorker
import java.util.concurrent.TimeUnit
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.lifecycleScope
import androidx.work.WorkRequest
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var refreshBtn: Button

    private val clearCacheRequest: WorkRequest =
        OneTimeWorkRequestBuilder<ClearCacheWorker>()
            .build()

    private val clearCachePeriodicRequest: WorkRequest =
        PeriodicWorkRequestBuilder<ClearCacheWorker>(15, TimeUnit.MINUTES)
            .build()

    private val imageDownloader = ImageDownloader()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main);
        downloadImage("https://daa.iict.ch/images/9.jpg")

        val recycler = findViewById<RecyclerView>(R.id.recycler)
        val adapter = ImageRecyclerAdapter()
        recycler.adapter = adapter
        recycler.layoutManager = GridLayoutManager(this, 3)
    }

    private fun downloadImage(url: String): Job {
        return lifecycleScope.launch {
            val bytes = imageDownloader.downloadImage(url)
            imageDownloader.decodeImage(bytes!!)
        }
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