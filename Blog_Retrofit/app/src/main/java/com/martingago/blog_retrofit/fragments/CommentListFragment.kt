package com.martingago.blog_retrofit.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.martingago.blog_retrofit.R
import com.martingago.blog_retrofit.adapter.CommentAdapter
import com.martingago.blog_retrofit.controller.CommentController
import com.martingago.blog_retrofit.databinding.FragmentCommentListBinding
import com.martingago.blog_retrofit.model.comment.CommentResponseItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class CommentListFragment : Fragment(R.layout.fragment_comment_list) {

    private var _binding : FragmentCommentListBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter : CommentAdapter
    private lateinit var  commentController: CommentController
    private val comments = mutableListOf<CommentResponseItem>()

    // Variable para almacenar el ID del post y manejar la carga dinámica de comentarios (Infinity scroll)
    private var postId: Long? = null
    private var isLoading = false
    private var currentPage = 0
    private var totalPages = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            postId = it.getLong("postId")
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCommentListBinding.inflate(inflater, container, false)

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = CommentAdapter(
            listComments = comments,
            fragmentManager = parentFragmentManager, // Pasar el FragmentManager
            postId = postId ?: 0L // Pasar el ID del post
        )
        initRecyclerView()
        postId?.let { fetchCommentsFromPublication(it) } // Cargar comentarios si existe un postId
    }


    private fun initRecyclerView(){
        binding.rvCommentsPublication.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCommentsPublication.adapter = adapter
        commentController = CommentController(requireContext())

        //Se ejecuta un listener del recyclerView para cargar más contenidos cuando el usuario se vaya acercando al final de la lista
        binding.rvCommentsPublication.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                // Detecta que el usuario se acerca al final de la lista y si no hay datos en carga <isLoading> realiza una nueva petición de datos.
                if (!isLoading && currentPage < totalPages) {

                    if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && //Detecta si el usuario ha desplazado hasta el final de la lista
                        firstVisibleItemPosition >= 0) { //Se detecta que haya almenos un elemento en la lista
                        loadMoreComments() // Carga más comentarios
                    }
                }
            }
        })

    }

    /**
     * @param postId identificador del post sobre el que se quieren cargar los comentarios
     * @param append valor booleano, si el false se trata de carga iniciar de datos, si es true se añaden nuevos comentarios
     * Esta funcion utiliza los valores que se obtienen de los pageableObjects para realizar una carga dinámica de datos a través de un scrollListener.
     * Mientras se estén cargando datos se pone el valor isLoading como true para evitar realizar multiples peticiones mientras el usuario scrollea
     *
     *
     */
    private fun fetchCommentsFromPublication(postId: Long, append: Boolean = false) {
        if (!append) {
            currentPage = 0 // Reiniciar la página si es una carga inicial
            totalPages = 1 // Reiniciar el total de páginas
            comments.clear() // Limpiar la lista existente
            adapter.notifyDataSetChanged() // Notificar cambios al adaptador
        }

        isLoading = true // Marcar que estamos cargando

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            try {
                Log.i("comments", "response que no toca")
                //Realiza la petición al servidor de los comentarios en una determinada página <currentPage>
                val response = withContext(Dispatchers.IO) {
                    commentController.fetchMainCommentsFromPublication(
                        postId,
                        currentPage,
                        8,
                        updateStatus = { currentAttempt, maxAttempts ->
                            showLoader(currentAttempt, maxAttempts)
                        }
                    )
                }
                if (response.success == true) {
                    //Si la respuesta es exitosa carga los comentarios recibidos en el recyclerView
                    response.objectResponse?.let { data ->
                        totalPages = data.totalPages // Actualizar el total de páginas
                        val newComments = data.content //Listado de comentarios

                        // Agregar los nuevos comentarios al listado
                        comments.addAll(newComments)

                        withContext(Dispatchers.Main) {
                            if (isAdded) adapter.notifyDataSetChanged()
                            binding.altMessageNoComments.visibility = if (comments.isEmpty()) View.VISIBLE else View.GONE
                        }
                    }
                } else {
                    showError(response.message ?: "Error loading comments")
                }
            } catch (e: Exception) {
                showError(e.toString())
            } finally {
                hideLoader()
                isLoading = false // Liberar el estado de carga
            }
        }
    }

    /**
     * funcion que carga más comentarios desde la base de datos
     * Por defecto esta función ejecutará la función {fetchCommentsFromPublication} con el valor de append True
     */
    private fun loadMoreComments() {
        if (currentPage < totalPages) {
            currentPage++ // Incrementar la página actual
            postId?.let { fetchCommentsFromPublication(it, append = true) }
        }
    }

    fun addComment(commentResponseItem: CommentResponseItem){
        Log.i("comment", "comment added: " + commentResponseItem.toString())
        adapter.addLocalComment(commentResponseItem)
        binding.rvCommentsPublication.scrollToPosition(0)
    }

    private fun loadReplyComments(commentId: Long, container: FrameLayout) {
        val addCommentFragment = AddCommentFragment.newInstance(postId ?: 0L, commentId)

        childFragmentManager.beginTransaction()
            .replace(container.id, addCommentFragment) // Reemplazar el contenedor específico
            .addToBackStack(null) // Agregar a la pila de retroceso
            .commit()
    }

    // Método para crear una instancia de CommentListFragment con el ID del post
    companion object {
        fun newInstance(postId: Long): CommentListFragment {
            val fragment = CommentListFragment()
            val args = Bundle()
            args.putLong("postId", postId)
            fragment.arguments = args
            return fragment
        }
    }


    private fun hideLoader() {
        if (isAdded && _binding != null) {
            _binding?.let {
                it.layoutLoadingArea.visibility = View.GONE
            }
        }
    }

    private fun showError(message : String) {
        if (isAdded && _binding != null) {
            _binding?.let {
                it.altMessageNoComments.visibility = View.VISIBLE
                it.altMessageNoComments.text = message
            }
            context?.let {
                Toast.makeText(it, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showLoader(currentAttempt : Int, maxAttempts : Int){
        if (isAdded && _binding != null) {
            _binding?.let {
                it.loadingText.text = getString(R.string.loading_comments, currentAttempt, maxAttempts)
                it.layoutLoadingArea.visibility = View.VISIBLE
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}