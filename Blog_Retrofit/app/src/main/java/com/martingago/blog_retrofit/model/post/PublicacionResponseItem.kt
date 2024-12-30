package com.martingago.blog_retrofit.model.post

import android.os.Parcelable
import com.martingago.blog_retrofit.model.post.PostDetailsResponseItem
import com.martingago.blog_retrofit.model.post.PostResponseItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class PublicacionResponseItem(
    val post: PostResponseItem,
    val postDetails: PostDetailsResponseItem,
) : Parcelable

