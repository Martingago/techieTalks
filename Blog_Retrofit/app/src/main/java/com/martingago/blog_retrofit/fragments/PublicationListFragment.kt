package com.martingago.blog_retrofit

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.martingago.blog_retrofit.adapter.PublicacionAdapter
import com.martingago.blog_retrofit.controller.PublicationController
import com.martingago.blog_retrofit.databinding.FragmentListPublicationsBinding
import com.martingago.blog_retrofit.fragments.SectionIndexFragmentDirections
import com.martingago.blog_retrofit.model.post.PublicacionResponseItem
import com.martingago.blog_retrofit.viewModel.PublicationViewModel

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PublicationListFragment : Fragment(R.layout.fragment_list_publications) {

    private var _binding: FragmentListPublicationsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: PublicacionAdapter
    private lateinit var publicationController: PublicationController
    private lateinit var publicationViewModel: PublicationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListPublicationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Obtén el ViewModel
        publicationViewModel = ViewModelProvider(requireActivity()).get(PublicationViewModel::class.java)

        // Inicializa RecyclerView
        initRecyclerView()

        // Cargar publicaciones si no están cargadas ya
        if (publicationViewModel.publications.isEmpty()) {
            loadPublications()
        } else {
            adapter.notifyDataSetChanged()
        }

        // Detecta el final del scroll para cargar más publicaciones
        binding.rvPublications.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1)) {
                    loadMorePublications()
                }
            }
        })
    }

    private fun initRecyclerView() {
        binding.rvPublications.layoutManager = LinearLayoutManager(requireContext())
        adapter = PublicacionAdapter(publicationViewModel.publications, childFragmentManager) { publication ->
            openPublicationDetails(publication)
        }
        binding.rvPublications.adapter = adapter
        publicationController = PublicationController(requireContext())
    }

    private fun openPublicationDetails(publication: PublicacionResponseItem) {
        findNavController().navigate(SectionIndexFragmentDirections.actionSectionIndexFragmentToSectionPublicationDetailsFragment(publication))
    }

    private fun loadPublications() {
        lifecycleScope.launch(Dispatchers.Main) {
            try {
                val response = withContext(Dispatchers.IO) {
                    publicationController.fetchPublicationList(
                        order = "desc",
                        page = publicationViewModel.currentPage,
                        size = 12,
                        updateStatus = { currentAttempt, maxAttempts ->
                            showLoader(currentAttempt, maxAttempts)
                        }
                    )
                }

                if (response.success == true && response.objectResponse?.content != null) {
                    val newPublications = response.objectResponse.content
                    publicationViewModel.publications.addAll(newPublications)
                    publicationViewModel.totalPages = response.objectResponse.totalPages

                    adapter.notifyDataSetChanged()
                } else {
                    showError(response.message)
                }
            } catch (e: Exception) {
                showError(e.toString())
            } finally {
                hideLoader()
            }
        }
    }

    private fun loadMorePublications() {
        if (publicationViewModel.currentPage < publicationViewModel.totalPages) {
            publicationViewModel.currentPage++
            loadPublications()
        }
    }

    private fun hideLoader() {
        _binding?.layoutLoadingArea?.visibility = View.GONE
    }

    private fun showError(message: String) {
        context?.let {
            Toast.makeText(it, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLoader(currentAttempt: Int, maxAttempt: Int) {
        _binding?.let {
            it.layoutLoadingArea.visibility = View.VISIBLE
            it.loadingText.text = getString(R.string.loading_data_attempt, currentAttempt, maxAttempt)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
