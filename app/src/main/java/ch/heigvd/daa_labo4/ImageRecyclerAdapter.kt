package ch.heigvd.daa_labo4

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import androidx.lifecycle.LifecycleCoroutineScope
import java.net.URL

/**
 * Adapter for images
 *
 * @author Nicolas Crausaz
 * @author Lazar Pavicevic
 * @author Maxime Scharwath
 */
class ImageRecyclerAdapter(
    _items: List<URL> = listOf(),
    private val scope: LifecycleCoroutineScope
) :
    RecyclerView.Adapter<ImageRecyclerAdapter.ViewHolder>() {

    private var items = listOf<URL>()

    init {
        items = _items
    }

    override fun getItemCount() = items.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val image = view.findViewById<ImageView>(R.id.image)
        private val progressBar = view.findViewById<ProgressBar>(R.id.progressbar)

        private var downloadJob: Job? = null

        fun bind(url: URL) {
            downloadJob = scope.launch {
                val cachedBitmap = getBitmap(url.path.substring(url.path.lastIndexOf('/') + 1), url)
                updateImageView(cachedBitmap)
            }
        }

        fun unbind() {
            downloadJob?.cancel()
            progressBar.visibility = View.VISIBLE
            image.visibility = View.INVISIBLE
        }

        private suspend fun updateImageView(bitmap: Bitmap) = withContext(Dispatchers.Main) {
            image.setImageBitmap(bitmap)
            image.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
        }

        private suspend fun getBitmap(filename: String, url: URL): Bitmap {
            var cachedBitmap = Cache.get(filename)
            if (cachedBitmap == null) {
                val downloader = ImageDownloader()
                val bytes = downloader.downloadImage(url)
                cachedBitmap = downloader.decodeImage(bytes!!)
                Cache.set(filename, cachedBitmap!!)
            }
           return  cachedBitmap
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_image, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        items[position].let { holder.bind(it) }
    }

    override fun onViewRecycled(holder: ViewHolder) {
        holder.unbind()
    }
}