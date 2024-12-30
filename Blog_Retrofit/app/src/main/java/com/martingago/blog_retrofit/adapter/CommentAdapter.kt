package com.martingago.blog_retrofit.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.martingago.blog_retrofit.R
import com.martingago.blog_retrofit.model.comment.CommentResponseItem
import com.martingago.blog_retrofit.viewHolder.CommentViewHolder

class CommentAdapter(
    private var listComments: MutableList<CommentResponseItem>,
    private val fragmentManager: FragmentManager,
    private val postId: Long,


    ) :RecyclerView.Adapter<CommentViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView = layoutInflater.inflate(R.layout.item_comentario, parent, false)
        return CommentViewHolder(itemView, fragmentManager, postId)
    }

    override fun getItemCount(): Int {
        return listComments.size
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = listComments[position]
        holder.bind(comment)
    }

    fun addLocalComment(commentResponseItem: CommentResponseItem){
        listComments.add(0, commentResponseItem)
        notifyItemInserted(0)
    }
    
}