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
import androidx.navigation.fragment.navArgs
import com.martingago.blog_retrofit.R
import com.martingago.blog_retrofit.controller.PublicationController
import com.martingago.blog_retrofit.model.post.PostRequestItem
import com.martingago.blog_retrofit.model.tag.TagResponseItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SectionEditPublicationFragment : Fragment() {

    private val args : SectionEditPublicationFragmentArgs by navArgs()

    private  lateinit var postTitle : EditText
    private  lateinit var postContent : EditText
    private  lateinit var btnUploadPost : Button
    private lateinit var publicationErrorMsg : TextView
    private  lateinit var publicationSuccessMsg : TextView
    private  lateinit var publicationController: PublicationController


    //Formulario y login
    private lateinit var formUpdatePublication : View
    private lateinit var loadingFormulario : View
    private lateinit var loadingMessage : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_section_edit_publication, container, false)
        publicationController = PublicationController(requireContext())
        postContent = view.findViewById(R.id.postContent)
        postTitle = view.findViewById(R.id.postTitle)
        btnUploadPost = view.findViewById(R.id.btnEditPost)
        //messages
        publicationErrorMsg = view.findViewById(R.id.editPublicationError)
        publicationSuccessMsg = view.findViewById(R.id.editPublicationSuccess)
        //Form y loading
        formUpdatePublication = view.findViewById(R.id.formAddPost)
        loadingFormulario = view.findViewById(R.id.loaderEditPublication)
        loadingMessage = view.findViewById(R.id.loadingMsg)
        loadSpinnerFragment(args.publicationData.post.tags)

        //Sets de text to update:
        postTitle.setText(args.publicationData.post.title)
        postContent.setText(args.publicationData.post.content)

        btnUploadPost.setOnClickListener {
            val postRequestItem = validateAndCreatePostRequest() ?: return@setOnClickListener
            uploadPostWithId(args.publicationData.post.id, postRequestItem)
        }
        return view
    }

    private fun uploadPostWithId(
        id : Long,
        postRequestItem: PostRequestItem
    ){
        viewLifecycleOwner.lifecycleScope.launch (Dispatchers.Main){
            try{
                val response = withContext(Dispatchers.IO){
                    publicationController.updatePublicationById(
                        id,
                        postRequestItem,
                        updateStatus = {currentAttempt, maxAttempts ->
                            showLoader(currentAttempt, maxAttempts)
                        }
                    )
                }
                if (response.success == true){
                    hiddeLoader(response.message)
                    val tagSpinnerFragment = childFragmentManager.findFragmentById(R.id.spinnerTagSelector) as? TagSpinnerFragment
                }else{
                    showError(response.message)
                }
            }catch (e : Exception){
                showError(e.toString())
            }
        }
    }

    /**
     * Obtiene los datos existentes en el formulario para crear un objeto de PostRquestItem que se enviará para actualizar datos en el servidor
     */
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


    private fun loadSpinnerFragment(tagList : List<TagResponseItem>? ){
        //Crea una instancia de TagSpinner que recibe los id de las tags a editar
        val tagSpinnerFragment = TagSpinnerFragment.newInstance(tagList)
        childFragmentManager.beginTransaction()
            .replace(R.id.spinnerTagSelector, tagSpinnerFragment)
            .commit()
    }

    private fun hiddeLoader(successMessage : String){
        loadingFormulario.visibility = View.GONE
        formUpdatePublication.visibility = View.VISIBLE
        publicationErrorMsg.visibility = View.GONE
        publicationSuccessMsg.visibility = View.VISIBLE
        publicationSuccessMsg.text = successMessage
    }
    private  fun showLoader(currentAttempt : Int, maxAttempt : Int){
        loadingFormulario.visibility = View.VISIBLE
        formUpdatePublication.visibility = View.GONE
        loadingMessage.text = getString(R.string.loading_updating_publication, currentAttempt, maxAttempt)
    }


    private fun showError(errorMessage : String) {
        loadingFormulario.visibility = View.GONE
        formUpdatePublication.visibility = View.VISIBLE
        publicationErrorMsg.text = errorMessage
        publicationErrorMsg.visibility = View.VISIBLE
        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
    }


}