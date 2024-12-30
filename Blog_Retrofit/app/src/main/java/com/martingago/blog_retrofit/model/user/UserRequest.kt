package com.martingago.blog_retrofit.model.user

import com.google.gson.annotations.SerializedName

data class UserRequest(
    @SerializedName("username") val username : String,
    @SerializedName("password") val password: String
)
