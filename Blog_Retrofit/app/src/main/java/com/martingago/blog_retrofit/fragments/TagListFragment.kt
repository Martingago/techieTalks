package com.martingago.blog_retrofit.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.martingago.blog_retrofit.R
import com.martingago.blog_retrofit.adapter.TagAdapter
import com.martingago.blog_retrofit.databinding.FragmentListTagBinding
import com.martingago.blog_retrofit.model.tag.TagResponseItem


class TagListFragment : Fragment(R.layout.fragment_list_tag) {

    private var _binding: FragmentListTagBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: TagAdapter
    private val tags = mutableListOf<TagResponseItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListTagBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Obtener las etiquetas desde los argumentos y configurar el adaptador
        val tagList = arguments?.getParcelableArrayList<TagResponseItem>(ARG_TAGS) ?: emptyList()
        tags.addAll(tagList)
        Log.i("publication", "Tags: "+ tagList.toString())
        initRecyclerView()
    }

    private fun initRecyclerView() {
        // Configuramos el RecyclerView para que cargue el listado de tags de forma horizontal
        binding.rvTags.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        adapter = TagAdapter(tags){ tag ->
            // navega a los detalles de la tag
        }
        binding.rvTags.adapter = adapter

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_TAGS = "tags"  // Definición de la constante
        fun newInstance(tagList: List<TagResponseItem>): TagListFragment {
            val fragment = TagListFragment()
            val args = Bundle().apply {
                putParcelableArrayList(ARG_TAGS, ArrayList(tagList))
            }
            fragment.arguments = args
            return fragment
        }
    }

}