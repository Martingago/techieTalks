package com.martingago.blog_retrofit.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.martingago.blog_retrofit.R
import com.martingago.blog_retrofit.model.tag.TagResponseItem
import com.martingago.blog_retrofit.viewHolder.TagViewHolder

class TagAdapter (

    private var listTags: List<TagResponseItem>,
    private val onItemClick: (TagResponseItem) -> Unit
): RecyclerView.Adapter<TagViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return TagViewHolder(layoutInflater.inflate(R.layout.fragment_tag, parent, false))
    }

    override fun getItemCount(): Int {
        return listTags.size
    }

    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
        val tag = listTags[position]
        holder.bind(tag)
        holder.itemView.setOnClickListener {
            onItemClick(tag)
        }
    }

}