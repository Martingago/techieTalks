package com.martingago.blog_retrofit.viewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.martingago.blog_retrofit.databinding.ItemTagTableRowBinding
import com.martingago.blog_retrofit.model.tag.TagResponseItem

class TagTableRowViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

    private val binding = ItemTagTableRowBinding.bind(itemView)

    fun bind(
        tag : TagResponseItem,
        onEditClick: (TagResponseItem) -> Unit,
        onDeleteClick: (TagResponseItem) -> Unit
    ){
        //Asignamos valores a las vistas
        binding.tvTitle.text = tag.name

        //Configuracion de botones
        binding.btnEdit.setOnClickListener { onEditClick(tag) }
        binding.btnDelete.setOnClickListener { onDeleteClick(tag) }
    }
}