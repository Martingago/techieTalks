package com.martingago.blog_retrofit.model.comment

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CommentResponseItem(
    val id: Long,
    @SerializedName("contenido") val content: String,
    @SerializedName("fechaComentario") val dateComment: String,
    @SerializedName("postId") val postId : Long,
    @SerializedName("userId") val userId: Long,
    @SerializedName("username") val username : String,
    @SerializedName("originId") val originId: Long?,
    @SerializedName("replyId") val replyId: Long?,
    @SerializedName("countReplies") val countReplies: Int
) : Parcelable
