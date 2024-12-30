package com.martingago.blog_retrofit.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.martingago.blog_retrofit.R
import com.martingago.blog_retrofit.model.post.PublicacionResponseItem
import com.martingago.blog_retrofit.viewHolder.PublicationViewHolder


class PublicacionAdapter(
    private var listPublications: List<PublicacionResponseItem>,
    private val fragmentManager: FragmentManager,
    private val onItemClick: (PublicacionResponseItem) -> Unit
) : RecyclerView.Adapter<PublicationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PublicationViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return PublicationViewHolder(layoutInflater.inflate(R.layout.item_publication, parent, false))
    }

    override fun getItemCount(): Int {
        return listPublications.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: PublicationViewHolder, position: Int) {
        val publication = listPublications[position]
        holder.bind(publication)
        // Asigna el click listener
        holder.itemView.setOnClickListener {
            onItemClick(publication)
        }
    }

}
