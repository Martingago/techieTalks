package com.martingago.blog_retrofit.model.tag

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class  TagRequestItem(
    @SerializedName("nombre") val name : String
) : Parcelable