package com.martingago.blog_retrofit.controller

import android.content.Context
import com.martingago.blog_retrofit.model.APIResponse
import com.martingago.blog_retrofit.model.comment.CommentRequestItem
import com.martingago.blog_retrofit.model.comment.CommentResponseItem
import com.martingago.blog_retrofit.model.pageable.PageableObject
import com.martingago.blog_retrofit.network.ApiCallsUtil
import com.martingago.blog_retrofit.repository.CommentRepository

class CommentController(private val context: Context) {

    private val commentRepository = CommentRepository(context)


    /**
     * Controller que realiza una petición al servidor añadiendo un nuevo comentario asociado a un post a la base de datos
     * La función ApiCallsUtil se encarga de manejar los intentos de llamada al servidor hasta obtener una respuesta.
     */
    suspend fun addNewComment(
        id: Long,
        commentRequestItem: CommentRequestItem,
        updateStatus: (currentAttempt: Int, maxAttempts: Int) -> Unit = { _, _ -> }
    ): APIResponse<CommentResponseItem?>{
        return ApiCallsUtil.callAPIWithRetry(
            updateStatus = updateStatus) {
            commentRepository.createComment(id, commentRequestItem)
        } as APIResponse<CommentResponseItem?>
    }


    /**
     * Controller que realiza una petición al servidor solicitando los comentarios existentes de una publicación proporcionada a través de su <id>
     * Se realiza una llamada a la función APICallsUtil que se encargará de gestionar los intentos de solicitud al servidor esperando una respuesta.
     */
    suspend fun fetchMainCommentsFromPublication(
        id : Long,
        page: Int,
        size : Int,
        updateStatus: (currentAttempt: Int, maxAttempts: Int) -> Unit = { _, _ -> }
    ): APIResponse<PageableObject<CommentResponseItem>?>{
        return ApiCallsUtil.callAPIWithRetry(
            updateStatus = updateStatus) {
            commentRepository.getMainCommentsFromPost(id, page, size)
        } as APIResponse<PageableObject<CommentResponseItem>?>
    }

    /**
     * Controller que realiza una petición al servidor solicitando las respuestas directas a un comentario proporcionado a través de su <commentId>
     * Se realiza una llamada a la funcion APICallsUtil para manejar los intentos de solicitud al servidor esperando una respuesta.
     */
    suspend fun  fetchMainRepliesFromComment(
        commentId: Long,
        page : Int,
        size: Int,
        updateStatus: (currentAttempt: Int, maxAttempts: Int) -> Unit = { _, _ -> }
    ): APIResponse<PageableObject<CommentResponseItem>>{
        return ApiCallsUtil.callAPIWithRetry(
            updateStatus = updateStatus) {
            commentRepository.loadRepliesFromComment(commentId, page, size)
        }as APIResponse<PageableObject<CommentResponseItem>>
    }

    /**
     * Controller que realiza una petición al servidor para eliminar un comentario a través de su <id> identificador.
     * Se realiza a través de la función ApiCallsUtil que manejará diferentes intentos de petición al servidor hasta obtener una respuesta.
     */
    suspend fun deleteCommentById(
        id: Long,
        updateStatus: (currentAttempt: Int, maxAttempts: Int) -> Unit = { _, _ -> }
    ) : APIResponse<Unit>{
        return ApiCallsUtil.callAPIWithRetry(
            updateStatus = updateStatus) {
            commentRepository.deleteCommentById(id)
        } as APIResponse<Unit>
    }

}