package com.martingago.blog_retrofit.model.post
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PostRequestItem(
    @SerializedName("titulo") val title : String,
    @SerializedName("contenido") val content : String,
    @SerializedName("tags") val tagList : List<Long>
) : Parcelable
