package com.martingago.blog_retrofit.model.user

import com.google.gson.annotations.SerializedName

data class UserRegisterRequest(
    @SerializedName("username") val username : String,
    @SerializedName("password") val password: String,
    @SerializedName("name") val name: String
)
