package ch.heigvd.daa_labo4

import androidx.lifecycle.LifecycleCoroutineScope
import ch.heigvd.daa_labo4.utils.Cache
import ch.heigvd.daa_labo4.utils.ImageDownloader
import kotlinx.coroutines.*
import java.io.File

class ImageRetriever(private val scope: LifecycleCoroutineScope, cacheDir: File) {
    init {
        Cache.setDir(cacheDir)
    }

    private val imgDownloader = ImageDownloader()

    suspend fun getImage(url: String) = withContext(Dispatchers.IO) {

        var cachedBitmap = Cache.get(url.hashCode().toString())

        if (cachedBitmap == null) {
            val img = imgDownloader.downloadImage(url)
            cachedBitmap = imgDownloader.decodeImage(img!!)
        }

        cachedBitmap
    }
}