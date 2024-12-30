package com.martingago.blog_retrofit.viewHolder

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.martingago.blog_retrofit.databinding.FragmentTagBinding
import com.martingago.blog_retrofit.model.tag.TagResponseItem


class TagViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val binding = FragmentTagBinding.bind(itemView)
    fun bind(tag: TagResponseItem){
        binding.tagContent.text = tag.name

    }
}