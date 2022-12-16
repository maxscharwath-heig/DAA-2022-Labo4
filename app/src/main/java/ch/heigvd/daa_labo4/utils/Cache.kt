package ch.heigvd.daa_labo4.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File

object Cache {
    private lateinit var cacheDir: File
    private var expirationDelay = 60 * 5000L // 5 minutes

    fun setDir(cacheDir: File) {
        Cache.cacheDir = cacheDir
    }

    fun setExpiration() {

    }

    fun get(name: String): Bitmap? {
        val file = File(cacheDir, name)
        if (file.exists() && file.canRead() && file.length() != 0L && !isExpired(file)) {
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

    private fun isExpired(file: File): Boolean {
        return file.lastModified() < System.currentTimeMillis() - expirationDelay
    }

    private fun File.writeBitmap(
        bitmap: Bitmap,
        format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
        quality: Int = 90
    ) {
        outputStream().use { out ->
            bitmap.compress(format, quality, out)
            out.flush()
        }
    }
}