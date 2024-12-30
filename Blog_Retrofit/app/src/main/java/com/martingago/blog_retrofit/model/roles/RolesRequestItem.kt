package com.martingago.blog_retrofit.model.roles

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class RolesRequestItem(
@SerializedName("roles") val roles: MutableList<Long>
) : Parcelable
