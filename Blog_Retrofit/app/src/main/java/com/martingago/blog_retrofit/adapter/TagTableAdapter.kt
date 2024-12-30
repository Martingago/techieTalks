package com.martingago.blog_retrofit.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.martingago.blog_retrofit.R
import com.martingago.blog_retrofit.model.tag.TagResponseItem
import com.martingago.blog_retrofit.viewHolder.TagTableRowViewHolder

class TagTableAdapter(
    private val tags : MutableList<TagResponseItem>,
    private val onEditClick: (TagResponseItem) -> Unit,
    private val onDeleteClick: (TagResponseItem) -> Unit
) : RecyclerView.Adapter<TagTableRowViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagTableRowViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tag_table_row, parent, false)
        return TagTableRowViewHolder(view)
    }

    override fun getItemCount(): Int = tags.size

    override fun onBindViewHolder(holder: TagTableRowViewHolder, position: Int) {
        val tag = tags[position]
        holder.bind(tag, onEditClick, onDeleteClick)
    }

    fun deleteLocalTagById(id: Long): Boolean {
        val index = tags.indexOfFirst { it.id == id }
        return if (index != -1) {
            tags.removeAt(index)
            notifyItemRemoved(index)
            true
        } else {
            false
        }
    }
}