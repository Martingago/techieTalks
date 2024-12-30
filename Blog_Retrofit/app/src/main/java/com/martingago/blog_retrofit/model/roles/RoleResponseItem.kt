package com.martingago.blog_retrofit.model.roles

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RoleResponseItem(
    val id : Long,
    val role : String
) : Parcelable
