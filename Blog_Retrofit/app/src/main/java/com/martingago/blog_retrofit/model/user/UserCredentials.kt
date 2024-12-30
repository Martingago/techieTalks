package com.martingago.blog_retrofit.model.user

data class UserCredentials(
    val id: Long,
    val username:String,
    val password : String,
    val userRoles: List<String>,
    val name:String
)
