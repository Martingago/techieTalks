package com.martingago.blog_retrofit.utils

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.martingago.blog_retrofit.R


object NavUtils {
    /**
     * Función para navegar al fragmento anterior, evitando regresar a Login o Register.
     * @param fragment El fragmento desde el cual se navega.
     */
    fun navigateBackAvoidingLoginRegister(fragment: Fragment) {
        val navController = fragment.findNavController()
        when (navController.previousBackStackEntry?.destination?.id) {
            R.id.sectionRegisterFragment, R.id.sectionLoginFragment -> {
                // Si el fragmento anterior es Register o Login, ir a la pantalla de índice
                navController.popBackStack(R.id.sectionIndexFragment, false)
            }
            else -> {
                // Si el fragmento anterior no es Login ni Register, navegar normalmente
                navController.popBackStack()
            }
        }
    }
}


