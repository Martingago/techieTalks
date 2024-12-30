package com.martingago.blog_retrofit.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.martingago.blog_retrofit.R
import com.martingago.blog_retrofit.controller.PublicationController
import com.martingago.blog_retrofit.model.APIResponse
import com.martingago.blog_retrofit.model.post.PostRequestItem
import com.martingago.blog_retrofit.model.post.PostResponseItem
import com.martingago.blog_retrofit.model.post.PublicacionResponseItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddPublicationFragment : Fragment() {

    private  lateinit var postTitle : EditText
    private  lateinit var postContent : EditText
    private  lateinit var btnUploadPost : Button
    private lateinit var publicationErrorMsg : TextView
    private  lateinit var publicationSuccessMsg : TextView
    private  lateinit var publicationController: PublicationController


    //Formulario y login
    private lateinit var formUploadPublication : View
    private lateinit var loadingFormulario : View
    private lateinit var loadingMessage : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_publication, container, false)
        publicationController = PublicationController(requireContext())
        postContent = view.findViewById(R.id.postContent)
        postTitle = view.findViewById(R.id.postTitle)
        btnUploadPost = view.findViewById(R.id.btnAddPost)
        //messages
        publicationErrorMsg = view.findViewById(R.id.addPublicationError)
        publicationSuccessMsg = view.findViewById(R.id.addPublicationSuccess)
        //Form y loading
        formUploadPublication = view.findViewById(R.id.formAddPost)
        loadingFormulario = view.findViewById(R.id.loaderAddPublication)
        loadingMessage = view.findViewById(R.id.loadingMsg)
        loadSpinnerFragment()
        btnUploadPost.setOnClickListener {
            val postRequestItem = validateAndCreatePostRequest() ?: return@setOnClickListener
            uploadNewPost(postRequestItem)
        }
        return  view
    }

    private fun validateAndCreatePostRequest(): PostRequestItem? {
        val title = postTitle.text.toString()
        val content = postContent.text.toString()

        // Obtiene el fragmento `TagSpinnerFragment` y llama a `getSelectedTagIds`
        val tagSpinnerFragment = childFragmentManager.findFragmentById(R.id.spinnerTagSelector) as? TagSpinnerFragment
        val tags = tagSpinnerFragment?.getSelectedTagIds() ?: emptyList()

        if (tags.isEmpty()) {
            Toast.makeText(requireContext(), "Please select at least one tag", Toast.LENGTH_SHORT).show()
            return null
        }

        return PostRequestItem(title, content, tags)
    }

    private fun uploadNewPost(
        postRequestItem: PostRequestItem
    ){
        viewLifecycleOwner.lifecycleScope.launch (Dispatchers.Main){
            try{
                val response = withContext(Dispatchers.IO){
                    publicationController.addNewPublication(
                        postRequestItem,
                        updateStatus = {currentAttempt, maxAttempts ->
                            showLoader(currentAttempt, maxAttempts)
                        }
                    )
                }
                if (response.success == true){
                    hiddeLoader(response.message)
                    clearForm()
                    val tagSpinnerFragment = childFragmentManager.findFragmentById(R.id.spinnerTagSelector) as? TagSpinnerFragment
                    tagSpinnerFragment?.restartSelectedTags() //Reinicia los tags
                }else{
                    showError(response.message)
                }
            }catch (e : Exception){
                showError(e.toString())
            }
        }
    }

    private fun loadSpinnerFragment(){
        val tagSpinnerFragment = TagSpinnerFragment()
        childFragmentManager.beginTransaction()
            .replace(R.id.spinnerTagSelector, tagSpinnerFragment)
            .commit()
    }

    private  fun clearForm(){
        postTitle.text.clear()
        postContent.text.clear()
    }
    private fun hiddeLoader(successMessage : String){
        loadingFormulario.visibility = View.GONE
        formUploadPublication.visibility = View.VISIBLE
        publicationErrorMsg.visibility = View.GONE
        publicationSuccessMsg.visibility = View.VISIBLE
        publicationSuccessMsg.text = successMessage
    }
    private  fun showLoader(currentAttempt : Int, maxAttempt : Int){
        loadingFormulario.visibility = View.VISIBLE
        formUploadPublication.visibility = View.GONE
        loadingMessage.text = getString(R.string.loading_adding_publication, currentAttempt, maxAttempt)
    }


    private fun showError(errorMessage : String) {
        loadingFormulario.visibility = View.GONE
        formUploadPublication.visibility = View.VISIBLE
        publicationErrorMsg.text = errorMessage
        publicationErrorMsg.visibility = View.VISIBLE
        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
    }

}