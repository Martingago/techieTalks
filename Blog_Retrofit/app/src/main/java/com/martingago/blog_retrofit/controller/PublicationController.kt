package com.martingago.blog_retrofit.controller

import android.content.Context
import com.martingago.blog_retrofit.model.APIResponse
import com.martingago.blog_retrofit.model.pageable.PageableObject
import com.martingago.blog_retrofit.model.post.PostRequestItem
import com.martingago.blog_retrofit.model.post.PublicacionResponseItem
import com.martingago.blog_retrofit.network.ApiCallsUtil
import com.martingago.blog_retrofit.repository.PublicationRepository

class PublicationController (private val context: Context){

    private val publicationRepository = PublicationRepository(context)


    /**
     * Controller que añade una nueva publicación a la Base de datos
     * Se requieren permisos de EDITOR para añadir nuevas publicaciones a la Base de datos.
     * @param postRequestItem objeto que contiene los datos de la publicación a añadir
     */
    suspend fun addNewPublication(
        postRequestItem: PostRequestItem,
        updateStatus: (currentAttempt: Int, maxAttempts: Int) -> Unit = { _, _ -> }
    ) : APIResponse<PublicacionResponseItem?> {
        return ApiCallsUtil.callAPIWithRetry(
            updateStatus = updateStatus) {
            publicationRepository.addNewPublication(postRequestItem)
        } as APIResponse<PublicacionResponseItem?>
    }

    /**
     * Controller que elimina una publicación cuyo identificador es pasado como parámetro
     * @param id identificador de la publicación a eliminar
     */
    suspend fun deletePublicationById(
        id: Long,
        updateStatus: (currentAttempt: Int, maxAttempts: Int) -> Unit = { _, _ -> }
    ): APIResponse<Unit>{
        return ApiCallsUtil.callAPIWithRetry(
            updateStatus = updateStatus) {
            publicationRepository.deletePostById(id)
        } as APIResponse<Unit>
    }

    /**
     * Controller que actualiza los datos de una publicación en la base de datos.
     * @param id identificador de la publicación a modificar
     * @param postRequestItem datos actualizados de la publicación a modificar
     */
    suspend fun updatePublicationById(
        id : Long,
        postRequestItem: PostRequestItem,
        updateStatus: (currentAttempt: Int, maxAttempts: Int) -> Unit = { _, _ -> }
    ): APIResponse<PublicacionResponseItem>{
        return ApiCallsUtil.callAPIWithRetry(
            updateStatus = updateStatus
        ){
            publicationRepository.updatePublicationById(id, postRequestItem)
        }as APIResponse<PublicacionResponseItem>
        }

    /**
     * Controller que solicita al endpoint un objeto <PageableObject> de <PublicationResponseItem>
     * @param order orden de ordenación de publicaciones: asc | desc
     * @param field atributo sobre el que se ordenan los elementos
     * @param page número de página
     * @param size tamaño de elementos de página
     */
    suspend fun fetchPublicationList(
        order: String,
        field : String? = null,
        page: Int,
        size: Int,
        updateStatus: (currentAttempt: Int, maxAttempts: Int) -> Unit = { _, _ -> }
    ): APIResponse<PageableObject<PublicacionResponseItem>>{
        val fieldCheck = field ?: "id"
        return ApiCallsUtil.callAPIWithRetry(
            updateStatus = updateStatus){
            publicationRepository.fetchPublications(order, fieldCheck, page, size)
        } as APIResponse<PageableObject<PublicacionResponseItem>>
    }
}