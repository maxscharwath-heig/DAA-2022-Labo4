package ch.heigvd.daa_labo4

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File

object Cache {
    private lateinit var cacheDir: File

    fun setDir(cacheDir: File) {
        this.cacheDir = cacheDir
    }

    fun get(name: String): Bitmap? {
        val file = File(cacheDir, name)
        if (file.exists()) {
            return BitmapFactory.decodeFile(file.path);
        }
        return null
    }

    fun set(name: String, image: Bitmap) {
        val file = File.createTempFile(name, ".jpg", cacheDir)
        file.writeBitmap(image)
    }

    fun clear() {
        cacheDir.deleteRecursively()
    }

    private fun File.writeBitmap(bitmap: Bitmap, format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG, quality: Int = 90) {
        outputStream().use { out ->
            bitmap.compress(format, quality, out)
            out.flush()
        }
    }
}