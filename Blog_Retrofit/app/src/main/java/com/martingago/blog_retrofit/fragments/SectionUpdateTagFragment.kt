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
import com.martingago.blog_retrofit.controller.TagController
import com.martingago.blog_retrofit.model.tag.TagRequestItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SectionUpdateTagFragment : Fragment() {

    private val args: SectionUpdateTagFragmentArgs by navArgs()

    private  lateinit var tagTitle : EditText
    private  lateinit var btnUploadTag : Button
    private lateinit var tagErrorMsg : TextView
    private  lateinit var tagSuccessMsg : TextView
    private  lateinit var tagController: TagController


    //Formulario y login
    private lateinit var formUpdateTag : View
    private lateinit var loadingForm : View
    private lateinit var loadingMessage : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_section_update_tag, container, false)
        tagController = TagController(requireContext())
        tagTitle = view.findViewById(R.id.tagname)
        btnUploadTag = view.findViewById(R.id.btnEditTag)
        //messages
        tagErrorMsg = view.findViewById(R.id.editTagError)
        tagSuccessMsg = view.findViewById(R.id.editTagSuccess)
        //Form y loaders
        formUpdateTag = view.findViewById(R.id.formUpdateTag)
        loadingForm = view.findViewById(R.id.loaderLayout)
        loadingMessage = view.findViewById(R.id.loadingMsg)
        //Establece datos del form
        tagTitle.setText(args.tagData.name)

        btnUploadTag.setOnClickListener {
            val updatedTag = TagRequestItem(tagTitle.text.toString())
            uploadTagWithId(args.tagData.id, updatedTag)
        }
        return view
    }

    private fun uploadTagWithId(
        id : Long,
        tagRequestItem: TagRequestItem
    ){
        viewLifecycleOwner.lifecycleScope.launch (Dispatchers.Main){
            try{
                val response = withContext(Dispatchers.IO){
                    tagController.updateTagById (
                        id,
                        tagRequestItem,
                        updateStatus = {currentAttempt, maxAttempts ->
                            showLoader(currentAttempt, maxAttempts)
                        }
                    )
                }
                if (response.success == true){
                    hiddeLoader(getString(R.string.updated_tag))
                }else{
                    showError(response.message)
                }
            }catch (e : Exception){
                showError(e.toString())
            }
        }
    }


    private fun hiddeLoader(successMessage : String){
        loadingForm.visibility = View.GONE
        formUpdateTag.visibility = View.VISIBLE
        tagErrorMsg.visibility = View.GONE
        tagSuccessMsg.visibility = View.VISIBLE
        tagSuccessMsg.text = successMessage
    }
    private  fun showLoader(currentAttempt : Int, maxAttempt : Int){
        loadingForm.visibility = View.VISIBLE
        formUpdateTag.visibility = View.GONE
        loadingMessage.text = getString(R.string.loading_updating_tag, currentAttempt, maxAttempt)
    }


    private fun showError(errorMessage : String) {
        loadingForm.visibility = View.GONE
        formUpdateTag.visibility = View.VISIBLE
        tagErrorMsg.text = errorMessage
        tagErrorMsg.visibility = View.VISIBLE
        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
    }


}