package ch.heigvd.daa_labo4

import android.graphics.Bitmap
import androidx.recyclerview.widget.DiffUtil

class ImageDiffCallback(private val oldList: List<Bitmap?>, private val newList: List<Bitmap?>) :
    DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size
    override fun getNewListSize() = newList.size
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition]?.sameAs(newList[newItemPosition]) ?: false // TODO
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old = oldList[oldItemPosition]
        val new = newList[newItemPosition]
        if (old != null && new != null) {
            return old::class == new::class && old.sameAs(new)
        }
        return true // TODO
    }
}