package com.martingago.blog_retrofit.viewHolder

import android.util.Log
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.martingago.blog_retrofit.R
import com.martingago.blog_retrofit.databinding.ItemComentarioBinding
import com.martingago.blog_retrofit.fragments.AddCommentFragment
import com.martingago.blog_retrofit.fragments.CommentResponsesListFragment
import com.martingago.blog_retrofit.model.comment.CommentResponseItem

class CommentViewHolder(
    itemView: View,
    private val fragmentManager: FragmentManager,
    private val postId: Long, // Es el post id
) : RecyclerView.ViewHolder(itemView) {

    private val binding = ItemComentarioBinding.bind(itemView)

    fun bind(comment: CommentResponseItem) {
        binding.userProfilePic.text = comment.username.take(1).uppercase()
        binding.commentMsg.text = comment.content
        binding.loadCommentsButton.text = itemView.context.getString(R.string.load_reply_comments, comment.countReplies)
        binding.userMessageOwner.text = comment.username

        //Genera un id unico para manejar la carga de responder a un comentario
        val uniqueResponseId = View.generateViewId()
        binding.repliesContainer.id = uniqueResponseId

        // Genera un id unico para manejar las carga de respuestas de comentarios
        val uniqueContainerId = View.generateViewId()
        binding.listCommentReplies.id = uniqueContainerId

        // Ocultar el botón de cargar respuestas si no hay respuestas
        if (comment.countReplies == 0) {
            binding.loadCommentsButton.visibility = View.GONE
        }

        // Manejar el clic en el botón de respuesta
        binding.btnReplyComment.setOnClickListener {
            // Verifica si el fragmento de respuestas ya existe
            Log.i("comments", "validando fragmento")
            val existingFragment = fragmentManager.findFragmentById(uniqueContainerId) as? CommentResponsesListFragment
            if (existingFragment == null) {

                Log.i("comments", "Creando fragmento en respuesta")
                // Carga un fragmento de respuestas vacío si no existe
                val emptyRepliesFragment = CommentResponsesListFragment.newInstance(postId, comment.id, true) //Genera la lista vacia
                val tagReplies = "repliesTag"+comment.id //Crea un tag para encontrarlo cuando se suban respuestas
                fragmentManager.beginTransaction()
                    .replace(uniqueContainerId, emptyRepliesFragment, tagReplies)
                    .addToBackStack(null)
                    .setReorderingAllowed(true)
                    .commit() // Commit inmediato para asegurarse de que el fragmento esté listo
            }

            val addCommentFragment = AddCommentFragment.newInstance(postId, comment.id)
            // Reemplazar el contenedor de respuestas con el fragmento
            fragmentManager.beginTransaction()
                .replace(uniqueResponseId, addCommentFragment)
                .addToBackStack(null)
                .setReorderingAllowed(true)
                .commit()
        }

        // Manejar el clic en el botón de cargar más respuestas/comentarios
        binding.loadCommentsButton.setOnClickListener {
            // Crear una nueva instancia del fragmento de respuestas, pasando el ID del comentario
            val targetFragment = CommentResponsesListFragment.newInstance(postId, comment.id)

            val tagReplies = "repliesTag"+comment.id //Crea un tag para encontrarlo cuando se suban respuestas
            // Reemplazar el contenedor único para este comentario usando id unico
            fragmentManager.beginTransaction()
                .replace(uniqueContainerId, targetFragment, tagReplies)
                .addToBackStack(null)
                .setReorderingAllowed(true)
                .commit()
        }
    }
}

