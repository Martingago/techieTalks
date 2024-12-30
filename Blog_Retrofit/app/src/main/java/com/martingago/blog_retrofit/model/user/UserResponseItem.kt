package com.martingago.blog_retrofit.model.user

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserResponseItem(
    @SerializedName("username") val username:String,
    @SerializedName("id") val id: Long,
    @SerializedName("roles")  val userRoles: List<String>,
    @SerializedName("name") val name:String
) : Parcelable
