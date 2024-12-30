package com.martingago.blog_retrofit.model.post


import android.os.Parcelable
import com.martingago.blog_retrofit.model.user.UserResponseItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class PostDetailsResponseItem (
    val fechaCreacion: String,
    val creador: UserResponseItem
) : Parcelable
