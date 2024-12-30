package com.martingago.blog_retrofit.viewModel

import androidx.lifecycle.ViewModel
import com.martingago.blog_retrofit.model.post.PublicacionResponseItem

class PublicationViewModel : ViewModel() {
    var currentPage: Int = 0 //Almacena la pagina en la que se quedo guardado el publicationViewModel
    var totalPages: Int = 1
    val publications = mutableListOf<PublicacionResponseItem>() // Lista para almacenar los datos
}