package com.martingago.blog_retrofit.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.martingago.blog_retrofit.R
import com.martingago.blog_retrofit.controller.CommentController
import com.martingago.blog_retrofit.model.comment.CommentRequestItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddCommentFragment : Fragment() {

    private lateinit var commentController: CommentController

    //Comment
    private lateinit var commentInput : EditText
    private lateinit var commentSubmit : ImageButton

    //Loading
    private  lateinit var commentLayout : View
    private  lateinit var loadingComment : View
    private lateinit var loadingMsg : TextView

    private var postId: Long? = null
    private var replyId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            postId = it.getLong(ARG_POST_ID)
            replyId = it.getLong(ARG_REPLY_ID, -1L).takeIf { it != -1L }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_comment, container, false)

        commentController = CommentController(requireContext())
        commentLayout = view.findViewById(R.id.commentLayout)
        loadingComment = view.findViewById(R.id.commentLoader)
        loadingMsg = view.findViewById(R.id.loadingMsg)

        commentInput = view.findViewById(R.id.commentInput)
        commentSubmit = view.findViewById(R.id.commentSubmit)

        commentSubmit.setOnClickListener {
            val commentText = commentInput.text.toString()
            if (commentText.isNotBlank() && postId != null) {
                submitComment(postId!!, commentText, replyId)
            } else {
                Toast.makeText(requireContext(), "Comment cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun submitComment(postId: Long, text: String, replyId: Long?) {
        viewLifecycleOwner.lifecycleScope.launch (Dispatchers.Main) {
            try {

                val commentRequest = CommentRequestItem(text, replyId)
                Log.i("comments", "comentario a subir: "+commentRequest.toString())
                val response = withContext(Dispatchers.IO){
                    commentController.addNewComment(postId,
                        commentRequest,
                        updateStatus = {currentAttempt, maxAttempts ->
                            showLoader(currentAttempt, maxAttempts)
                        })
                }
                    if(response.success == true){
                        response.objectResponse?.let { newComment ->
                            if(replyId != null){
                                val tagReplies = "repliesTag"+replyId //Tag del fragment generado en CommentViewHolder
                                var parentFragment = parentFragmentManager.findFragmentByTag(tagReplies) as? CommentResponsesListFragment
                                parentFragment?.addComment(newComment)
                            }else{
                                // Busca el fragmento de la lista de comentarios se trata comentario raiz
                                val parentFragment = parentFragmentManager.findFragmentByTag("fragmentCommentSection") as? CommentListFragment
                                parentFragment?.addComment(newComment) // Añade el comentario localmente
                            }

                            showToast("Comment added successfully")
                            // Limpia el campo de entrada
                            commentInput.text.clear()
                        }

                    }else{
                        Log.i("messages", response.message)
                        showToast(response.message)
                    }
                    hideLoader()
            }catch (e : Exception){
                Log.e("error", e.toString())
            }
        }
    }

    companion object {
        private const val ARG_POST_ID = "postId"
        private const val ARG_REPLY_ID = "replyId"

        fun newInstance(postId: Long, replyId: Long? = null) =
            AddCommentFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_POST_ID, postId)
                    replyId?.let { putLong(ARG_REPLY_ID, it) }
                }
            }
    }

    private fun showToast(message : String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private  fun showLoader(currentAttempt : Int, maxAttempt : Int){
        loadingComment.visibility = View.VISIBLE
        commentLayout.visibility = View.GONE
        loadingMsg.text = getString(R.string.updating_comment, currentAttempt, maxAttempt)
    }

    private fun hideLoader(){
        loadingComment.visibility = View.GONE
        commentLayout.visibility = View.VISIBLE
    }

}
