package com.martingago.blog_retrofit.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.martingago.blog_retrofit.R
import com.martingago.blog_retrofit.model.post.PublicacionResponseItem


class SectionPublicationDetailsFragment : Fragment() {

    private val args : SectionPublicationDetailsFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_section_publication_details, container, false)
        loadChildFragment(args.publicationData)
        return view
    }


    private fun loadChildFragment(dataObject: PublicacionResponseItem) {
        //Fragmento detalles de publicación
        val publicationDetailsFragment = PublicationDetailsFragment.newInstance(dataObject) // Crea una instancia de publicationDetails

        //Fragmento nevos comentarios
        val addCommentFragment = AddCommentFragment.newInstance(dataObject.post.id, null)

        //Fragmento listado de comentarios
        val commentSectionFragment = CommentListFragment.newInstance(dataObject.post.id) //Se pasa el ID del post para cargar comentarios
        childFragmentManager.beginTransaction()
            .replace(R.id.fragmentPublicationDetails, publicationDetailsFragment, "fragmentPublicationDetails")
            .replace(R.id.fragmentAddNewCommentSectionPublicationDetails, addCommentFragment, "fragmentAddNewCommentSection")
            .replace(R.id.fragmentCommentSectionPublicationDetails, commentSectionFragment, "fragmentCommentSection")
            .commit()
    }

}