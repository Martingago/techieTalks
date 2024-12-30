package com.martingago.blog_retrofit.model.comment

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CommentRequestItem(
    @SerializedName("contenido") val content : String,
    @SerializedName("replyId") val replyId: Long?
) : Parcelable
