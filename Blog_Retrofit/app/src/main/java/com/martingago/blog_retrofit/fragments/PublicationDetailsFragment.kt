package com.martingago.blog_retrofit.fragments

import DateFormatterUtil
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.martingago.blog_retrofit.R
import com.martingago.blog_retrofit.model.post.PublicacionResponseItem


class PublicationDetailsFragment : Fragment() {

    //Objeto heredado que recibe desde el padre
    private lateinit var dataObject: PublicacionResponseItem

    //Elementos a mostrar
    private lateinit var titlePublication : TextView
    private lateinit var contentPublication : TextView
    private lateinit var creatorPublication : TextView
    private lateinit var datePublication : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Recuperar el objeto 'dataObject' desde los argumentos del fragmento
        arguments?.let {
            dataObject = it.getParcelable(ARG_DATA) ?: throw IllegalArgumentException("Data object is required")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_publication_details, container, false)
        titlePublication = view.findViewById(R.id.titlePublication)
        contentPublication = view.findViewById(R.id.contentPublication)
        creatorPublication = view.findViewById(R.id.userCreator)
        datePublication = view.findViewById(R.id.dateOfCreation)

        titlePublication.text = dataObject.post.title
        contentPublication.text = dataObject.post.content

        creatorPublication.text = dataObject.postDetails.creador.name
        datePublication.text = DateFormatterUtil.formatToDateString(dataObject.postDetails.fechaCreacion)

        initTagListFragment() //Inicializa el listado de tags
        return view
    }

    //Inicializa el fragmento de tagListFragment con los datos recibidos en el bundle principal
    private fun initTagListFragment(){
        val tagList = dataObject.post.tags ?: emptyList() //Si el objeto que se recibe es null se envia un emptyList
        val tagListFragment = TagListFragment.newInstance(tagList)

        childFragmentManager.beginTransaction()
            .replace(R.id.listTagsPublication, tagListFragment)
            .commit()
    }

    companion object {
        private const val ARG_DATA = "dataObject"
        fun newInstance(data: PublicacionResponseItem): PublicationDetailsFragment {
            val fragment = PublicationDetailsFragment()
            val args = Bundle().apply {
                putParcelable(ARG_DATA, data)
            }
            fragment.arguments = args
            return fragment
        }
    }


}