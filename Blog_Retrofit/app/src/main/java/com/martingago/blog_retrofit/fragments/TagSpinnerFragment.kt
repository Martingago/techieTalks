package com.martingago.blog_retrofit.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.martingago.blog_retrofit.R
import com.martingago.blog_retrofit.controller.TagController
import com.martingago.blog_retrofit.model.tag.TagResponseItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TagSpinnerFragment : Fragment() {

    // Lista de tags preseleccionados en lugar de solo IDs
    private var preselectedTags: MutableList<TagResponseItem>? = null

    private lateinit var tagController: TagController
    private lateinit var selectedTags: MutableList<TagResponseItem> // Tags seleccionados
    private lateinit var tagList: MutableList<TagResponseItem> // Lista total de tags
    private lateinit var tvSelectedTags: TextView
    private lateinit var btnOpenTagSelector: Button
    private lateinit var flSelectedTags: ViewGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preselectedTags = arguments?.getParcelableArrayList("preselectedTags")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tag_spinner, container, false)
        tagController = TagController(requireContext())
        tvSelectedTags = view.findViewById(R.id.tvSelectedTags)
        btnOpenTagSelector = view.findViewById(R.id.btnOpenTagSelector)
        flSelectedTags = view.findViewById(R.id.flSelectedTags)
        selectedTags = mutableListOf() // Tags seleccionados desde el selector
        tagList = mutableListOf() // Lista general de tags

        btnOpenTagSelector.setOnClickListener {
            if (tagList.isNotEmpty()) {
                showTagSelectorDialog()
            } else {
                Toast.makeText(requireContext(), "Loading tags, please wait...", Toast.LENGTH_SHORT).show()
            }
        }
        loadTags()

        // Si hay tags preseleccionados, agrégalos a `selectedTags`
        preselectedTags?.let {
            selectedTags.addAll(it)
            updateSelectedTags()
        }

        return view
    }

    // Obtener las tags seleccionadas
    fun getSelectedTagIds(): List<Long> {
        return selectedTags.map { it.id } // Mapea los objetos seleccionados a sus IDs
    }

    // Reiniciar las tags seleccionadas
    fun restartSelectedTags() {
        selectedTags.clear()
        loadTagListFragment(emptyList())
    }

    private fun updateSelectedTags() {
        // Actualizar el fragmento con las tags seleccionadas
        if (selectedTags.isNotEmpty()) {
            loadTagListFragment(selectedTags)
        } else {
            // Limpia el contenedor si no hay tags seleccionadas
            childFragmentManager.beginTransaction().replace(R.id.flSelectedTags, Fragment()).commit()
        }
    }

    private fun loadTagListFragment(selectedTags: List<TagResponseItem>) {
        val tagListFragment = TagListFragment.newInstance(selectedTags)
        childFragmentManager.beginTransaction()
            .replace(R.id.flSelectedTags, tagListFragment)
            .commit()
    }

    private fun loadTags() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            try {
                var currentPage = 0
                var lastPage = false
                do {
                    val response = tagController.fetchPageableTags(
                        currentPage,
                        size = 12,
                        updateStatus = { currentAttempt, maxAttempts ->
                            Log.i("tag", currentAttempt.toString())
                        })
                    if (response.success == true && response.objectResponse != null) {
                        tagList.addAll(response.objectResponse.content)
                        currentPage++
                        lastPage = response.objectResponse.last
                    } else lastPage = true
                } while (!lastPage)
            } catch (e: Exception) {
                Log.e("tag", "Error while fetching tags ", e)
            }
        }
    }

    private fun showTagSelectorDialog() {
        // Nombres de las tags para mostrar en el diálogo
        val tagNames = tagList.map { it.name }.toTypedArray()
        val selected = BooleanArray(tagList.size) { index ->
            selectedTags.contains(tagList[index])
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Select Tags")
            .setMultiChoiceItems(tagNames, selected) { _, which, isChecked ->
                val tag = tagList[which]
                if (isChecked) {
                    // Añade la tag si se selecciona
                    if (!selectedTags.contains(tag)) {
                        selectedTags.add(tag)
                    }
                } else {
                    selectedTags.remove(tag)
                }
            }
            .setPositiveButton("OK") { dialog, _ ->
                updateSelectedTags()
                dialog.dismiss()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    companion object {
        fun newInstance(preselectedTags: List<TagResponseItem>?): TagSpinnerFragment {
            val fragment = TagSpinnerFragment()
            val args = Bundle()
            args.putParcelableArrayList("preselectedTags", ArrayList(preselectedTags))
            fragment.arguments = args
            return fragment
        }
    }
}
