package com.martingago.blog_retrofit.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.fragment.findNavController
import com.martingago.blog_retrofit.R

class SectionRegisterFragment : Fragment() {

    private lateinit var switchToLogin: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_section_register, container, false)

        // Carga el fragmento del register form
        loadRegisterFormFragment()

        switchToLogin = view.findViewById(R.id.switchToLogin)
        switchToLogin.setOnClickListener{
            navigateToLogin()
        }

        return view
    }


    private fun loadRegisterFormFragment(){
        val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
        val registerFragment = RegisterFragment()
        transaction.replace(R.id.authFragmentContainer, registerFragment)
        transaction.commit()
    }

    private fun navigateToLogin() {
        // Navegar usando el NavController
        findNavController().navigate(R.id.action_sectionRegisterFragment_to_sectionLoginFragment)
    }

}