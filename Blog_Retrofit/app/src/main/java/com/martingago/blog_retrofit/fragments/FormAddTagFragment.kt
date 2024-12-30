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
import com.martingago.blog_retrofit.controller.TagController
import com.martingago.blog_retrofit.model.tag.TagRequestItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FormAddTagFragment : Fragment() {

    private lateinit var tagController: TagController
    private lateinit var tagEditText : EditText
    private lateinit var btnUploadTag : Button

    //Mensajes de respuesta
    private lateinit var successMessage : TextView
    private lateinit var errorMessage : TextView
    //Loader y formulario
    private lateinit var  sectionForm : View
    private lateinit var sectionLoader : View
    private lateinit var loadingMsg : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_form_add_tag, container, false)
        tagController = TagController(requireContext())
        tagEditText = view.findViewById(R.id.tagnameEditText)
        btnUploadTag = view.findViewById(R.id.btnAddTag)
        successMessage = view.findViewById(R.id.addTagSuccess)
        errorMessage = view.findViewById(R.id.addTagError)
        sectionForm = view.findViewById(R.id.formAddTag)
        sectionLoader = view.findViewById(R.id.sectionLoader)
        loadingMsg = view.findViewById(R.id.loadingMsg)

        btnUploadTag.setOnClickListener {
            val tagRequestItem = TagRequestItem(tagEditText.text.toString())
            addNewTag(tagRequestItem)
        }

        return view
    }

    private fun addNewTag(tagRequestItem: TagRequestItem){
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main){
            try{
                val response = withContext(Dispatchers.IO){
                    tagController.addNewTag(
                        tagRequestItem,
                        updateStatus = {currentAttempt, maxAttempts ->
                            showLoader(currentAttempt, maxAttempts)
                        }
                    )
                }
                if(response.success == true){
                    hideLoader(getString(R.string.success_add_tag))
                    clearForm()
                }else{
                    showError(response.message)
                }
            }catch (e : Exception){
                showError(e.toString())
            }
        }
    }
    private fun showLoader(currentAttempt : Int, maxAttempts : Int){
        sectionForm.visibility = View.GONE
        sectionLoader.visibility = View.VISIBLE
        loadingMsg.text = getString(R.string.loading_adding_tag, currentAttempt, maxAttempts)
    }
    private  fun hideLoader(message : String){
        sectionForm.visibility = View.VISIBLE
        sectionLoader.visibility = View.GONE
        successMessage.text = message
        successMessage.visibility = View.VISIBLE
        errorMessage.visibility = View.GONE
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showError(message: String) {
        sectionLoader.visibility = View.GONE
        sectionForm.visibility = View.VISIBLE
        errorMessage.text = message
        errorMessage.visibility = View.VISIBLE
        successMessage.visibility = View.GONE
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun clearForm(){
        tagEditText.text.clear()
    }

}