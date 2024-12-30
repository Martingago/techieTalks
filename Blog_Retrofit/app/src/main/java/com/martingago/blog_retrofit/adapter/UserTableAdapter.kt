package com.martingago.blog_retrofit.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.martingago.blog_retrofit.R
import com.martingago.blog_retrofit.model.user.UserResponseItem
import com.martingago.blog_retrofit.viewHolder.UserTableRowViewHolder

class UserTableAdapter(
    private val users: MutableList<UserResponseItem>,
    private val onEditClick : (UserResponseItem) -> Unit,
    private val onDeleteClick : (UserResponseItem) -> Unit
): RecyclerView.Adapter<UserTableRowViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserTableRowViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user_table_row, parent, false)
        return UserTableRowViewHolder(view)
    }

    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: UserTableRowViewHolder, position: Int) {
        val user = users[position]
        holder.bind(user, onEditClick, onDeleteClick)
    }

    fun deleteLocalUserById(id: Long): Boolean {
        val index = users.indexOfFirst { it.id == id }
        return if (index != -1) {
            users.removeAt(index)
            notifyItemRemoved(index)
            true
        } else {
            false
        }
    }


}