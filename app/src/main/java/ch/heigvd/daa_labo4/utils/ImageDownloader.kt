package ch.heigvd.daa_labo4.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.net.URL

class ImageDownloader {
    companion object {
        private const val TAG = "ImageDownloader"
    }

    suspend fun downloadImage(url: String): ByteArray? = withContext(Dispatchers.IO) {
        try {
            val test = URL(url).readBytes()

            delay(1000)
            test
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