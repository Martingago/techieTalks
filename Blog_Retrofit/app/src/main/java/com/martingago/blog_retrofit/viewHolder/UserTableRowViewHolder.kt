package com.martingago.blog_retrofit.viewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.martingago.blog_retrofit.databinding.ItemUserTableRowBinding
import com.martingago.blog_retrofit.model.user.UserResponseItem

class UserTableRowViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

    private val binding = ItemUserTableRowBinding.bind(itemView)

    fun bind(
        userResponseItem: UserResponseItem,
        onEditClick: (UserResponseItem) -> Unit,
        onDeleteClick: (UserResponseItem) -> Unit,
    ){
        binding.tvUsername.text = userResponseItem.username
        binding.tvName.text = userResponseItem.name
        binding.tvRoles.text = userResponseItem.userRoles?.joinToString(", ") ?: "Sin roles asignados"


        //Configuración de botones:
        binding.btnEdit.setOnClickListener { onEditClick(userResponseItem) }
        binding.btnDelete.setOnClickListener { onDeleteClick(userResponseItem) }
    }

}