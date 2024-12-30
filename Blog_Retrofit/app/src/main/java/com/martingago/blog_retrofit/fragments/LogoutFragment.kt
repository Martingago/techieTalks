package com.martingago.blog_retrofit.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.martingago.blog_retrofit.R
import com.martingago.blog_retrofit.interfaces.UserLoginStatusChangeListener
import com.martingago.blog_retrofit.network.SharedPreferencesManager
import kotlinx.coroutines.launch

class LogoutFragment : Fragment() {

    private lateinit var sharedPreferencesManager: SharedPreferencesManager
    private lateinit var logoutButton : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_logout, container, false)
        sharedPreferencesManager = SharedPreferencesManager(requireContext())
        logoutButton = view.findViewById(R.id.btnLogout)

        //Limpia las credenciales de usuario
        logoutButton.setOnClickListener{
            clearCredentialsAndNavigate()
        }
        return view
    }

    //Funcion que limpia las credenciales del usuario, actualiza el menu de la aplicación y lo envia al inicio
    private fun clearCredentialsAndNavigate() {
        sharedPreferencesManager.clearCredentials() //Limpia las credenciales del usuario

        // Notificar a la actividad para que actualice el menú
        (activity as? UserLoginStatusChangeListener)?.updateMenuBasedOnLoginStatus()
        lifecycleScope.launch {
            Toast.makeText(requireContext(), getString(R.string.message_logout_success), Toast.LENGTH_SHORT).show()
        }
        findNavController().navigate(R.id.action_global_sectionIndexFragment)
    }

}