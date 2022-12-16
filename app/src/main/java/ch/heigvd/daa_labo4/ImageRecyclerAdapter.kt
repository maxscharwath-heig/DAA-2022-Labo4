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

/**
 * Adapter for images
 *
 * @author Nicolas Crausaz
 * @author Lazar Pavicevic
 * @author Maxime Scharwath
 */
class ImageRecyclerAdapter(_items: List<String> = listOf(), private val imgRetriever: ImageRetriever) :
    RecyclerView.Adapter<ImageRecyclerAdapter.ViewHolder>() {

    private var items = listOf<String>()

    init {
        items = _items
    }

    override fun getItemCount() = items.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val image = view.findViewById<ImageView>(R.id.image)
        private val progressBar = view.findViewById<ProgressBar>(R.id.progressbar)

        private var downloadJob: Job? = null

        fun bind(bitmap: Bitmap) {

            /*
            runBlocking {
                val job = async(Dispatchers.IO) {
                    imgRetriever.getImage(url)
                }

                val btmp = job.await()
                image.setImageBitmap(btmp)
                image.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
            }
*/
/*
            if (cachedBitmap == null) {
                CoroutineScope(Dispatchers.IO).launch {
                    val downloader = ImageDownloader()
                    val bytes = downloader.downloadImage(url)
                    val bitmap = downloader.decodeImage(bytes!!)
                    Cache.set(url.hashCode().toString(), bitmap!!)
                    CoroutineScope(Dispatchers.Main).launch {
                        image.setImageBitmap(bitmap)
                        image.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                    }

                }
            } else {
                CoroutineScope(Dispatchers.Main).launch {
                    image.setImageBitmap(cachedBitmap)
                    image.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                }
            }

 */
        }

        fun unbind() {
            downloadJob?.cancel()
            progressBar.visibility = View.VISIBLE
            image.visibility = View.INVISIBLE
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