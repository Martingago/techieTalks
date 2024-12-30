package com.martingago.blog_retrofit.viewHolder

import android.os.Build
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.martingago.blog_retrofit.databinding.ItemPublicationBinding
import com.martingago.blog_retrofit.fragments.TagListFragment
import com.martingago.blog_retrofit.model.post.PostResponseItem
import com.martingago.blog_retrofit.model.post.PublicacionResponseItem
import com.martingago.blog_retrofit.model.tag.TagResponseItem
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale


class PublicationViewHolder(
    itemView: View,
) : RecyclerView.ViewHolder(itemView) {

    private val binding = ItemPublicationBinding.bind(itemView)


    @RequiresApi(Build.VERSION_CODES.O)
    fun bind(publication: PublicacionResponseItem) {
        val originalDateString = publication.postDetails.fechaCreacion

        val formattedDate = if (!originalDateString.isNullOrBlank()) {
            try {
                val dateFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
                val date = ZonedDateTime.parse(originalDateString, dateFormatter)
                date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault()))
            } catch (e: Exception) {
                // Log the exception and provide a fallback
                Log.e("PublicationViewHolder", "Error parsing date: $originalDateString", e)
                "Unknown Date"
            }
        } else {
            "Unknown Date"
        }

        val listTags : List<TagResponseItem> =  publication.post.tags ?: emptyList()
        val formattedTags = listTags.map { it.name }.joinToString(separator = ", ")

        // Assign values to the views
        binding.tituloTextView.text = publication.post.title ?: "No Title"
        binding.contenidoTextView.text = publication.post.content ?: "No Content"
        binding.fechaTextView.text = formattedDate
        binding.creadorTextView.text = publication.postDetails.creador.name ?: "Unknown Creator"
        binding.tagsTextView.text = formattedTags

    }

}