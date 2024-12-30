package com.martingago.blog_retrofit.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import com.martingago.blog_retrofit.R


class SectionAddPublicationFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =inflater.inflate(R.layout.fragment_section_add_publication, container, false)
        loadPublicationFragments()
        return view
    }

    private fun loadPublicationFragments(){
        val transaction : FragmentTransaction = childFragmentManager.beginTransaction()
        val addPublicationFragment = AddPublicationFragment()
        transaction.replace(R.id.contentFragmentContainer, addPublicationFragment)
        transaction.commit()
    }


}