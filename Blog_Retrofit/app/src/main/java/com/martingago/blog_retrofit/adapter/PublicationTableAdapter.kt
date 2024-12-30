package com.martingago.blog_retrofit.adapter


import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import com.martingago.blog_retrofit.R
import com.martingago.blog_retrofit.model.post.PublicacionResponseItem
import com.martingago.blog_retrofit.viewHolder.PublicationTableRowViewHolder

class PublicationTableAdapter(
    private val publications: MutableList<PublicacionResponseItem>,
    private val onVisitClick: (PublicacionResponseItem) -> Unit,
    private val onEditClick: (PublicacionResponseItem) -> Unit,
    private val onDeleteClick: (PublicacionResponseItem) -> Unit
) : RecyclerView.Adapter<PublicationTableRowViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PublicationTableRowViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_publication_table_row, parent, false)
        return PublicationTableRowViewHolder(view)
    }

    override fun onBindViewHolder(holder: PublicationTableRowViewHolder, position: Int) {
        val publication = publications[position]
        holder.bind(publication, onVisitClick, onEditClick, onDeleteClick)
    }

    override fun getItemCount(): Int = publications.size

    fun deleteLocalPublicationById(id: Long): Boolean {
        val index = publications.indexOfFirst { it.post.id == id }
        return if (index != -1) {
            publications.removeAt(index)
            notifyItemRemoved(index)
            true
        } else {
            false
        }
    }
}