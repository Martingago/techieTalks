package com.martingago.blog_retrofit.interfaces

interface UserLoginStatusChangeListener {
    fun updateMenuBasedOnLoginStatus()  // Método que actualizará el menú cuando el usuario cambie de estado de sesión
}