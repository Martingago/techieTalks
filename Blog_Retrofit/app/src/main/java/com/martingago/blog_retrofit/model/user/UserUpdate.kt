package com.martingago.blog_retrofit.model.user

import com.google.gson.annotations.SerializedName

data class UserUpdate(
    @SerializedName("name") val name : String,
    @SerializedName("password") val password: String
)
