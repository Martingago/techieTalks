package com.martingago.blog_retrofit.model

import com.google.gson.annotations.SerializedName

//Data clas para manejar todas las respuestas procedentes del API
data class APIResponse<T>(
    @SerializedName("success") val success : Boolean?, //Se admite el valor null para cuando el servidor no responde
    @SerializedName("statusMessage") val message : String,
    @SerializedName("objectResponse") val objectResponse : T?
)
