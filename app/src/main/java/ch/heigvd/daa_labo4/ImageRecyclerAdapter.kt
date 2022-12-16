package ch.heigvd.daa_labo4

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import ch.heigvd.daa_labo4.utils.Cache
import ch.heigvd.daa_labo4.utils.ImageDownloader
import kotlinx.coroutines.*
import androidx.lifecycle.LifecycleCoroutineScope

/**
 * Adapter for images
 *
 * @author Nicolas Crausaz
 * @author Lazar Pavicevic
 * @author Maxime Scharwath
 */
class ImageRecyclerAdapter(_items: List<Int> = listOf(), private val scope: LifecycleCoroutineScope) :
    RecyclerView.Adapter<ImageRecyclerAdapter.ViewHolder>() {

    private var items = listOf<Int>()

    init {
        items = _items
    }

    override fun getItemCount() = items.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val image = view.findViewById<ImageView>(R.id.image)
        private val progressBar = view.findViewById<ProgressBar>(R.id.progressbar)

        private var downloadJob: Job? = null

        fun bind(position: Int) {
            downloadJob = scope.launch{

                var cachedBitmap = Cache.get(position.toString() + "test")

                if (cachedBitmap == null) {
                    val test = ImageDownloader()
                    val bytes = test.downloadImage("https://daa.iict.ch/images/$position.jpg")
                    cachedBitmap = test.decodeImage(bytes!!)
                    Cache.set(position.toString() + "test", cachedBitmap!!)
                }

                updateImageView(cachedBitmap)
                image.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
            }
        }
/*
        fun unbind() {
            downloadJob?.cancel()
            progressBar.visibility = View.VISIBLE
            image.visibility = View.INVISIBLE
        }

 */
        suspend fun updateImageView(bitmap: Bitmap) = withContext(Dispatchers.Main){
            image.setImageBitmap(bitmap)
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
        // holder.unbind()
    }
}