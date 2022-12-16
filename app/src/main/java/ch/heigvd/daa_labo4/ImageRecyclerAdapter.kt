package ch.heigvd.daa_labo4

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Adapter for images
 *
 * @author Nicolas Crausaz
 * @author Lazar Pavicevic
 * @author Maxime Scharwath
 */
class ImageRecyclerAdapter(_items: List<String> = listOf()) :
    RecyclerView.Adapter<ImageRecyclerAdapter.ViewHolder>() {

    var items = listOf<String>()
        set(value) {
            val diffCallback = ImageDiffCallback(items, value)
            val diffItems = DiffUtil.calculateDiff(diffCallback)
            field = value
            diffItems.dispatchUpdatesTo(this)
        }

    init {
        items = _items
    }

    override fun getItemCount() = items.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val image = view.findViewById<ImageView>(R.id.image)
        private val progressBar = view.findViewById<ProgressBar>(R.id.progressbar)

        fun bind(url: String) {
            // TODO Call the cache to get the image
            if (Cache.get(url.hashCode().toString()) == null) {
                CoroutineScope(Dispatchers.IO).launch {
                    val downloader = ImageDownloader()
                    val bytes = downloader.downloadImage(url)
                    val bitmap = downloader.decodeImage(bytes!!)
                    Cache.set(url.hashCode().toString(), bitmap!!)

                    CoroutineScope(Dispatchers.Main).launch {
                        image.setImageBitmap(bitmap)
                        progressBar.visibility = View.GONE
                    }
                }
            }
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
}