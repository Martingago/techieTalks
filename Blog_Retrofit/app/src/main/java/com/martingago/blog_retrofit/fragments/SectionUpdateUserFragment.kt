package com.martingago.blog_retrofit.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentTransaction
import com.martingago.blog_retrofit.R
import com.martingago.blog_retrofit.network.SharedPreferencesManager


class SectionUpdateUserFragment : Fragment() {

    private lateinit var sectionInfoText : TextView
    private lateinit var  sharedPreferencesManager: SharedPreferencesManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_section_update_user, container, false)
        sharedPreferencesManager = SharedPreferencesManager(requireContext())
        sectionInfoText = view.findViewById(R.id.sectionInfoText)
        val name = sharedPreferencesManager.getName()
        sectionInfoText.text = getString(R.string.profile_update_info, name) //Texto personalizado con el nombre del usuario

        loadFormUpdateFragment() //Carga el fragmento de actualizar datos del usuario
        return view
    }

    private fun loadFormUpdateFragment() {
        val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
        val formUpdateUserFragment = FormUpdateUserFragment()
        transaction.replace(R.id.updateUserFragmentContainer, formUpdateUserFragment)
        transaction.commit()
    }

}