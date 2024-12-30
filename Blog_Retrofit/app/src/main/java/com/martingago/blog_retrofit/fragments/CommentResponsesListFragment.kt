package com.martingago.blog_retrofit.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.martingago.blog_retrofit.R
import com.martingago.blog_retrofit.adapter.CommentAdapter
import com.martingago.blog_retrofit.controller.CommentController
import com.martingago.blog_retrofit.databinding.FragmentCommentResponsesListBinding
import com.martingago.blog_retrofit.model.comment.CommentResponseItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class CommentResponsesListFragment : Fragment() {

    private var _binding : FragmentCommentResponsesListBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter : CommentAdapter
    private lateinit var  commentController: CommentController
    private val comments = mutableListOf<CommentResponseItem>()

    // Variable para almacenar el ID del post y manejar la carga dinámica de comentarios
    private var postId: Long? = null
    private var commentId: Long? = null
    private var isEmptyInit: Boolean = false //Flag para que se genere este fragment sin cargar los datos
    private var isLoading = false
    private var currentPage = 0
    private var totalPages = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            postId = it.getLong("postId")
            commentId = it.getLong("commentId")
            isEmptyInit = it.getBoolean("isEmptyInit", false) // Leer la bandera
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCommentResponsesListBinding.inflate(inflater, container, false)

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = CommentAdapter(
            listComments = comments,
            fragmentManager = parentFragmentManager,
            postId = postId ?: 0L,
        )
        initRecyclerView()

        // Cargar datos solo si no es inicialización vacía
        if (!isEmptyInit) {
            commentId?.let { fetchRepliesFromComment(it) }
        }
    }

    private fun initRecyclerView(){
        binding.rvRespuestasComentarios.layoutManager = LinearLayoutManager(requireContext())
        binding.rvRespuestasComentarios.adapter = adapter
        commentController = CommentController(requireContext())


    }

    /**
     * @param postId identificador del post sobre el que se quieren cargar los comentarios
     * @param append valor booleano, si el false se trata de carga iniciar de datos, si es true se añaden nuevos comentarios
     * Esta funcion utiliza los valores que se obtienen de los pageableObjects para realizar una carga dinámica de datos a través de un scrollListener.
     * Mientras se estén cargando datos se pone el valor isLoading como true para evitar realizar multiples peticiones mientras el usuario scrollea
     *
     *
     */
    private fun fetchRepliesFromComment(commentId: Long, append: Boolean = false) {
        Log.i("comments", "cargando respuestas al comentario: " + commentId)
        if (!append) {
            currentPage = 0 // Reiniciar la página si es una carga inicial
            totalPages = 1 // Reiniciar el total de páginas
            comments.clear() // Limpiar la lista existente
            adapter.notifyDataSetChanged() // Notificar cambios al adaptador
        }

        isLoading = true // Marcar que estamos cargando

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            try {
                //Realiza la petición al servidor de los comentarios en una determinada página <currentPage>
                val response = withContext(Dispatchers.IO) {
                    commentController.fetchMainRepliesFromComment(
                        commentId,
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
                        adapter.notifyDataSetChanged()
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
    private fun loadMoreCommentsReplies() {
        if (currentPage < totalPages) {
            currentPage++ // Incrementar la página actual
            commentId?.let { fetchRepliesFromComment(it, append = true) }
        }
    }

    fun addComment(commentResponseItem: CommentResponseItem){
        Log.i("comment", "comment added: " + commentResponseItem.toString())
        adapter.addLocalComment(commentResponseItem)
        binding.rvRespuestasComentarios.scrollToPosition(0)
    }


    // Método para crear una instancia de CommentListFragment con el ID del post
    companion object {
        fun newInstance(postId: Long,commentId: Long, isEmptyInit: Boolean = false): CommentResponsesListFragment {
            val fragment = CommentResponsesListFragment()
            val args = Bundle()
            args.putLong("postId", postId)
            args.putLong("commentId", commentId)
            args.putBoolean("isEmptyInit", isEmptyInit)
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