package com.martingago.blog_retrofit.fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.martingago.blog_retrofit.R
import com.martingago.blog_retrofit.adapter.TagTableAdapter
import com.martingago.blog_retrofit.controller.TagController
import com.martingago.blog_retrofit.model.post.PublicacionResponseItem
import com.martingago.blog_retrofit.model.tag.TagResponseItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TagTableListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var tagTableAdapter: TagTableAdapter
    private lateinit var tagController: TagController
    private lateinit var loadingArea: View
    private lateinit var loadingText: TextView

    private val tags = mutableListOf<TagResponseItem>()
    private var isLoading = false
    private var currentPage = 0
    private var totalPages = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_tag_table_list, container, false)
        tagController = TagController(requireContext())
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.tagRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        loadingArea = view.findViewById(R.id.layoutLoadingArea)
        loadingText = view.findViewById(R.id.loadingText)

        tagTableAdapter = TagTableAdapter(
            tags = tags,
            onEditClick = { tag -> editTag(tag) },
            onDeleteClick = { tag -> deleteTag(tag) }
        )
        recyclerView.adapter = tagTableAdapter

        initRecyclerView()
        loadTags() // Carga inicial de datos
    }

    private fun initRecyclerView() {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && currentPage < totalPages) {
                    if (visibleItemCount + firstVisibleItemPosition >= totalItemCount &&
                        firstVisibleItemPosition >= 0
                    ) {
                        loadMoreTags()
                    }
                }
            }
        })
    }

    private fun loadTags(append: Boolean = false) {
        if (!append) {
            currentPage = 0
            totalPages = 1
            tags.clear()
            tagTableAdapter.notifyDataSetChanged()
        }

        isLoading = true
        lifecycleScope.launch(Dispatchers.Main) {
            try {
                val response = withContext(Dispatchers.IO) {
                    tagController.fetchPageableTags(
                        page = currentPage,
                        size = 12,
                        updateStatus = { currentAttempt, maxAttempts ->
                            showLoader(currentAttempt, maxAttempts)
                        }
                    )
                }
                if (response.success == true) {
                    response.objectResponse?.let { data ->
                        totalPages = data.totalPages
                        val newTags = data.content //Se actualiza el valor de paginas totales
                        tags.addAll(newTags)

                        withContext(Dispatchers.Main) {
                            if (isAdded) tagTableAdapter.notifyDataSetChanged()
                        }
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Error: ${response.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    "Error al cargar las tags",
                    Toast.LENGTH_SHORT
                ).show()
            } finally {
                hideLoader()
                isLoading = false
            }
        }
    }

    private fun loadMoreTags() {
        if (currentPage < totalPages) {
            currentPage++
            loadTags(append = true)
        }
    }

    private fun editTag(tagResponseItem: TagResponseItem) {
        findNavController().navigate(SectionManageTagsFragmentDirections.actionSectionManageTagsFragmentToSectionUpdateTagFragment(tagResponseItem))

    }

    private fun deleteTag(tagResponseItem: TagResponseItem) {
        showDeleteConfirmationDialog(tagResponseItem)
    }


    /**
     * Función que muestra un Dialog de confirmación cuando el usuario quiere eliminar una tag.
     * Además también muestra un loader durante el proceso de eliminación de datos.
     * Una vez eliminada la tag, se actualiza la lista local de tags.
     */
    private fun showDeleteConfirmationDialog(tagResponseItem: TagResponseItem) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.delete_tag))
        builder.setMessage(getString(R.string.message_confirm_delete_tag, tagResponseItem.name))

        // Código para el boton de confirmar, se llama al controller que gestiona eliminación de tags y se muestra un loader
        builder.setPositiveButton("Sí") { dialog, _ ->
            lifecycleScope.launch(Dispatchers.Main) {
                try {
                    //Muestra el dialog parametrizado
                    showLoaderDelete(tagResponseItem.name)
                    val response = withContext(Dispatchers.IO) {
                        tagController.deleteTagById(tagResponseItem.id)
                    }
                    //Muestra mensaje personalizado en base a la respuesta del servidor
                    if (response.success == true) {
                        tagTableAdapter.deleteLocalTagById(tagResponseItem.id)
                        Toast.makeText(requireContext(), getString(R.string.delete_tag_confirm), Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                    }
                    hideLoader()
                } catch (e: Exception) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.delete_tag_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            dialog.dismiss()
        }

        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss() // Cerrar el diálogo
        }
        // Mostrar el diálogo
        builder.create().show()
    }

    private fun hideLoader() {
        loadingArea.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }

    private fun showLoader(currentAttempt: Int, maxAttempt: Int) {
        loadingArea.visibility = View.VISIBLE
        loadingText.text = getString(R.string.loading_data_attempt, currentAttempt, maxAttempt)
    }

    private fun showLoaderDelete(name: String) {
        loadingArea.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        loadingText.text = getString(R.string.loading_deleting_tag, name)
    }
}
