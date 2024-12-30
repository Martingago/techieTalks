package com.martingago.blog_retrofit.model.post

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.martingago.blog_retrofit.model.tag.TagResponseItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class
PostResponseItem(
    val id:Long,
    @SerializedName("titulo") val title: String,
    @SerializedName("contenido") val content: String,
    @SerializedName("tagsList") val tags: List<TagResponseItem>?
) : Parcelable
