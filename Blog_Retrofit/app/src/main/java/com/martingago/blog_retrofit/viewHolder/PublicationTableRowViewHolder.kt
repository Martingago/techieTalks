package com.martingago.blog_retrofit.viewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.martingago.blog_retrofit.databinding.ItemPublicationTableRowBinding
import com.martingago.blog_retrofit.model.post.PublicacionResponseItem

class PublicationTableRowViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

    private val binding = ItemPublicationTableRowBinding.bind(itemView)

    fun bind(
        publication: PublicacionResponseItem,
        onVisitClick: (PublicacionResponseItem) -> Unit,
        onEditClick: (PublicacionResponseItem) -> Unit,
        onDeleteClick: (PublicacionResponseItem) -> Unit
    ) {
        // Asignamos los valores a las vistas
        binding.tvTitle.text = publication.post.title
        binding.tvCreator.text = publication.postDetails.creador.name

        // Configuramos los botones
        binding.btnVisit.setOnClickListener { onVisitClick(publication) }
        binding.btnEdit.setOnClickListener { onEditClick(publication) }
        binding.btnDelete.setOnClickListener { onDeleteClick(publication) }
    }
}