package com.martingago.blog_retrofit.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.fragment.findNavController
import com.martingago.blog_retrofit.R

class SectionLoginFragment : Fragment() {
    private lateinit var switchToRegister: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_section_login, container, false)

        // Cargar el formulario de login inicial
        loadLoginFormFragment()

        // Configurar el botón para cambiar a registro
        switchToRegister = view.findViewById(R.id.switchToRegister)
        switchToRegister.setOnClickListener {
            navigateToRegister()
        }

        return view
    }

    private fun loadLoginFormFragment() {
        val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
        val loginFragment = FormLoginFragment()
        transaction.replace(R.id.authFragmentContainer, loginFragment)
        transaction.commit()
    }

    private fun navigateToRegister() {
        // Navegar usando el NavController
        findNavController().navigate(R.id.action_sectionLoginFragment_to_sectionRegisterFragment)
    }
}