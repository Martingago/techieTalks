package com.martingago.blog_retrofit.model.tag

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class TagResponseItem(
    @SerializedName("id") val  id : Long,
    @SerializedName("nombre")val  name: String,
) : Parcelable
