package ch.heigvd.daa_labo4

import android.graphics.Bitmap
import java.io.File

// TODO: give path
object Cache {
    private val cache = HashMap<String, Bitmap>()
    private lateinit var cacheDir: File

    fun setDir(cacheDir: File) {
        this.cacheDir = cacheDir
    }

    fun get(name: String): Bitmap? {
        if (cache.containsKey(name)) {
            return null
        }

        // TODO: get the file

        val file = File(cacheDir, name)

        return null
    }

    fun set(name: String, image: Bitmap) {
        cache[name] = image
        File.createTempFile("name", ".jpg", cacheDir)
    }

    fun clear() {
        cache.clear()
    }

    private fun storeBitmapAsFile(bitmap: Bitmap) {
        // https://stackoverflow.com/questions/15662258/how-to-save-a-bitmap-on-internal-storage

    }
}