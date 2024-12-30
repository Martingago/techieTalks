package com.martingago.blog_retrofit.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.*
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.martingago.blog_retrofit.R
import com.martingago.blog_retrofit.adapter.PublicationTableAdapter
import com.martingago.blog_retrofit.controller.PublicationController
import com.martingago.blog_retrofit.model.post.PublicacionResponseItem
import com.martingago.blog_retrofit.model.tag.TagResponseItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PublicationTableListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var publicationAdapter: PublicationTableAdapter
    private lateinit var publicationController: PublicationController

    private lateinit var loadingArea : View
    private lateinit var loadingText : TextView

    private val publications = mutableListOf<PublicacionResponseItem>()

    private var isLoading = false
    private var currentPage = 0
    private var totalPages = 1 //Se inicializa en 1 y luego se actualiza con su valor real

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_publication_table_list, container, false)
        publicationController = PublicationController(requireContext())
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.publicationRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        loadingArea = view.findViewById(R.id.layoutLoadingArea)
        loadingText = view.findViewById(R.id.loadingText)

        // Inicializar el adaptador vacío
        publicationAdapter = PublicationTableAdapter(
            publications = publications, // Lista vacía por ahora
            onVisitClick = { publication -> visitPublication(publication) },
            onEditClick = { publication -> editPublication(publication) },
            onDeleteClick = { publication -> deletePublication(publication) }
        )
        recyclerView.adapter = publicationAdapter

        loadPublications()
        initRecyclerView()
    }

    private fun initRecyclerView(){
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
                        loadMorePublications()
                    }
                }
            }
        })
    }

    private fun loadPublications(append : Boolean = false) {
        if (!append) {
            currentPage = 0
            totalPages = 1
            publications.clear()
            publicationAdapter.notifyDataSetChanged()
        }

        isLoading = true

        lifecycleScope.launch(Dispatchers.Main) {
            try {
                // Llamada a la API para obtener el listado de publicaciones
                val response = withContext(Dispatchers.IO) {
                    publicationController.fetchPublicationList(
                        order = "desc",
                        page = currentPage,
                        size = 24, //
                        updateStatus = { currentAttempt, maxAttempts ->
                            showLoader(currentAttempt, maxAttempts) // Actualizar el estado en la UI en cada intento
                        }
                    )
                }
                if (response.success == true && response.objectResponse?.content != null) {
                    response.objectResponse.let { publicationResponse ->
                        totalPages = publicationResponse.totalPages // Se actualiza el total de páginas
                        // Actualizar el adaptador con las publicaciones obtenidas
                        val newPublications = publicationResponse.content
                        publications.addAll(newPublications)

                        // Notifica al adaptador de que los datos han cambiado
                        withContext(Dispatchers.Main) {
                            if (isAdded) publicationAdapter.notifyDataSetChanged()
                        }
                    }
                } else {
                    makeText(requireContext(), "Error: ${response.message}", LENGTH_SHORT).show()
                }
                hideLoader()
            } catch (e: Exception) {
                makeText(requireContext(), "Error al cargar las publicaciones", LENGTH_SHORT).show()
            }finally {
                hideLoader()
                isLoading = false
            }
        }
    }

    private fun loadMorePublications() {
        if (currentPage < totalPages) {
            currentPage++
            loadPublications(append = true)
        }
    }

    /**
     * Función que permite visitar una publicación desde el area de edición.
     * Utiliza el navController para cargar el fragmento de SectionPublicationDetailsFragment
     */
    private fun visitPublication(publicationResponseItem: PublicacionResponseItem) {
        findNavController().navigate(SectionManagePublicationsFragmentDirections.actionSectionManagePublicationsFragmentToSectionPublicationDetailsFragment(publicationResponseItem))
    }


    private fun deletePublication(publicationResponseItem: PublicacionResponseItem) {
        showDeleteConfirmationDialog(publicationResponseItem)
    }

    private fun editPublication(publicationResponseItem: PublicacionResponseItem) {
        findNavController().navigate(SectionManagePublicationsFragmentDirections.actionSectionManagePublicationsFragmentToSectionEditPublicationFragment(publicationResponseItem))
    }

    /**
     * Función que muestra un Dialog de confirmación cuando el usuario quiere eliminar una publicación.
     * Además también muestra un loader durante el proceso de eliminación de datos.
     * Una vez eliminada la publicación, se actualiza la lista local de publicaciones.
     */
    private fun showDeleteConfirmationDialog(publicationResponseItem: PublicacionResponseItem) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.delete_publication))
        builder.setMessage(getString(R.string.message_confirm_delete_publication, publicationResponseItem.post.title))

        // Código para el boton de confirmar, se llama al controller que gestiona eliminación de publicaciones y se muestra un loader
        builder.setPositiveButton("Sí") { dialog, _ ->
            lifecycleScope.launch(Dispatchers.Main) {
                try {
                    //Muestra el dialog parametrizado
                    showLoaderDelete(publicationResponseItem.post.title)
                    val response = withContext(Dispatchers.IO) {
                        publicationController.deletePublicationById(publicationResponseItem.post.id)
                    }
                    //Muestra mensaje personalizado en base a la respuesta del servidor
                    if (response.success == true) {
                        publicationAdapter.deleteLocalPublicationById(publicationResponseItem.post.id)
                        Toast.makeText(requireContext(), getString(R.string.delete_publication_confirm), Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), getString(R.string.delete_publication_error), Toast.LENGTH_SHORT).show()
                    }
                    hideLoader()
                } catch (e: Exception) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.delete_publication_error),
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

    private fun hideLoader(){
        loadingArea.visibility= View.GONE
        recyclerView.visibility= View.VISIBLE
    }
    private fun showLoader(currentAttempt : Int, maxAttempt: Int){
        loadingArea.visibility = View.VISIBLE
        loadingText.text = getString(R.string.loading_data_attempt, currentAttempt, maxAttempt)
    }

    private fun showLoaderDelete(name : String){
        loadingArea.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        loadingText.text = getString(R.string.loading_deleting_publication, name)
    }
}
