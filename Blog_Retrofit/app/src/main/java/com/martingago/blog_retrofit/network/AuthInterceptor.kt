package com.martingago.blog_retrofit.network

import android.content.Context
import android.util.Base64
import okhttp3.Interceptor
import okhttp3.Response


class AuthInterceptor(private val context: Context) : Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        // Se Obtiene las credenciales del usuario logueado
        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("USERNAME", null)
        val password = sharedPreferences.getString("PASSWORD", null)

        //Se obtienen los headers que por defecto se van a enviar en la petición HTTP
        val originalRequest = chain.request()
        val builder = originalRequest.newBuilder()

        //Si existen datos en las credenciales locales se modifica el header añadiendo la basic auth como parámetro extra en nuestra solicitud HTTP
        if(username !=null && password != null){
            val credentials = "$username:$password"
            val basicAuth = "Basic " + Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP)
            builder.addHeader("Authorization", basicAuth)
        }
        //Se devuelven los nuevos headers con el basic auth incluido
        val newRequest = builder.build()
        return chain.proceed(newRequest)
    }
}