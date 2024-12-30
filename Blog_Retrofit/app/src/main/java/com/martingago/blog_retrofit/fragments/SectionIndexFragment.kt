package com.martingago.blog_retrofit.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.martingago.blog_retrofit.PublicationListFragment
import com.martingago.blog_retrofit.R
import com.martingago.blog_retrofit.viewModel.PublicationViewModel

class SectionIndexFragment : Fragment() {

    private lateinit var publicationViewModel: PublicationViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Inicializar el viewModel
        publicationViewModel = ViewModelProvider(requireActivity())[PublicationViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_section_index, container, false)
        loadListPublicationsFragment()
        return view
    }

    private fun loadListPublicationsFragment() {
        val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
        val publicationListFragment = PublicationListFragment()

        // Pasamos la lista de publicaciones desde el ViewModel
        publicationListFragment.arguments = Bundle().apply {
            putParcelableArrayList("publications", ArrayList(publicationViewModel.publications))
        }

        transaction.replace(R.id.contentFragmentContainer, publicationListFragment)
        transaction.commit()
    }

}