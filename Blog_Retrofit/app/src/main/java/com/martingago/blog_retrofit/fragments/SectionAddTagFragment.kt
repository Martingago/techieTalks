package com.martingago.blog_retrofit.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import com.martingago.blog_retrofit.R


class SectionAddTagFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_section_add_tag, container, false)
        loadAddTagFragment()
        return view
    }

    private fun loadAddTagFragment(){
        val transaction : FragmentTransaction = childFragmentManager.beginTransaction()
        val addTagFragment = FormAddTagFragment()
        transaction.replace(R.id.contentFragmentContainer, addTagFragment)
        transaction.commit()
    }
}