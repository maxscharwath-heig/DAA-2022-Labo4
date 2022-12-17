package ch.heigvd.daa_labo4

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.net.URL

/**
 * Helpers for image downloading & decoding
 *
 * @author Nicolas Crausaz
 * @author Lazar Pavicevic
 * @author Maxime Scharwath
 */
class ImageDownloader {
    companion object {
        private val TAG = ImageDownloader::class.qualifiedName
    }

    suspend fun downloadImage(url: URL): ByteArray? = withContext(Dispatchers.IO) {
        try {
            delay(500)
            url.readBytes()
        } catch (e: Exception) {
            Log.w(TAG, "Error downloading image", e)
            null
        }
    }

    suspend fun decodeImage(bytes: ByteArray): Bitmap? = withContext(Dispatchers.Default) {
        try {
            BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        } catch (e: Exception) {
            Log.w(TAG, "Error decoding image", e)
            null
        }
    }
}