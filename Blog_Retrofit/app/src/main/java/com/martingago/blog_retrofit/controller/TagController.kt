package com.martingago.blog_retrofit.controller

import android.content.Context
import com.martingago.blog_retrofit.model.APIResponse
import com.martingago.blog_retrofit.model.pageable.PageableObject
import com.martingago.blog_retrofit.model.tag.TagRequestItem
import com.martingago.blog_retrofit.model.tag.TagResponseItem
import com.martingago.blog_retrofit.network.ApiCallsUtil
import com.martingago.blog_retrofit.repository.TagRepository

class TagController (private val context: Context) {

    //Inicialización del repository
    private val tagRepository = TagRepository(context)


    /**
     * Controller que realiza una petición al servidor solicitando un objeto <PageableResponse> de <TagResponseItems>
     * La función realiza una llamada a la función de ApiCallsUtils que gestiona un numero limitado de llamadas al servidor esperando una respuesta váldia
     */
    suspend fun fetchPageableTags(
        page : Int,
        size : Int,
        updateStatus: (currentAttempt: Int, maxAttempts: Int) -> Unit = { _, _ -> },
    ): APIResponse<PageableObject<TagResponseItem>>{
        return ApiCallsUtil.callAPIWithRetry (updateStatus = updateStatus){
            tagRepository.fetchPaginationTags(page, size)
        } as APIResponse<PageableObject<TagResponseItem>>
    }

    /**
     * Controller que realiza una petición al servidor añadiendo una nueva tag <TagRequestItem> y en caso de éxito devuelve un <TagResponseItem>
     * La función realiza una llamada a la funcion ApiCallsUtils gestionando varios intentos de inserción de datos esperando una respuesta válida del servidor
     */
    suspend fun addNewTag(
        tagRequestItem: TagRequestItem,
        updateStatus: (currentAttempt: Int, maxAttempts: Int) -> Unit = { _, _ -> },
    ): APIResponse<TagResponseItem>{
        return ApiCallsUtil.callAPIWithRetry(updateStatus = updateStatus) {
            tagRepository.addNewTag(tagRequestItem)
        } as APIResponse<TagResponseItem>
    }

    /**
     * Controller que realiza una petición al servidor eliminando una tag a través de su <id>.
     * El servidor devolverá una APIResponse cuyo <objectResponse> será siempre null: <Unit>
     */
    suspend fun deleteTagById(
        id : Long,
        updateStatus: (currentAttempt: Int, maxAttempts: Int) -> Unit = { _, _ -> },
    ): APIResponse<Unit>{
        return ApiCallsUtil.callAPIWithRetry(updateStatus = updateStatus) {
            tagRepository.deleteTagById(id)
        } as APIResponse<Unit>
    }

    /**
     * Controller que realiza una petición al servidor actualizando una tag a través de su <id>
     * El servidor devolverá una APIResponse cuyo <objectResponse> será la tag actualizada <TagResponseItem>
     */
    suspend fun updateTagById(
        id : Long,
        tagRequestItem: TagRequestItem,
        updateStatus: (currentAttempt: Int, maxAttempts: Int) -> Unit = { _, _ -> },
    ) : APIResponse<TagResponseItem> {
        return  ApiCallsUtil.callAPIWithRetry(updateStatus = updateStatus) {
            tagRepository.updateTagById(id, tagRequestItem)
        } as APIResponse<TagResponseItem>
    }

}