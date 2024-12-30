package com.martingago.blog_retrofit.repository

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.martingago.blog_retrofit.R
import com.martingago.blog_retrofit.model.APIResponse
import com.martingago.blog_retrofit.model.comment.CommentRequestItem
import com.martingago.blog_retrofit.model.comment.CommentResponseItem
import com.martingago.blog_retrofit.model.pageable.PageableObject
import com.martingago.blog_retrofit.network.ApiClient
import com.martingago.blog_retrofit.service.CommentService
import java.net.SocketTimeoutException

class CommentRepository(private val context: Context) {

    private val commentServiceNoAuth : CommentService
    private val commentServiceAuth : CommentService

    init {
        commentServiceNoAuth = ApiClient.getInstanceWithoutAuth().create(CommentService::class.java)
        commentServiceAuth = ApiClient.getInstanceWithAuth(context).create(CommentService::class.java)
    }

    /**
     * Función que realiza una llamada al servidor para devolver un objeto <PageableObject> que contiene un listado de <CommentResponseItem>
     * con los comentarios principales existentes en una publicación
     * @param id identificador del post sobre el que obtener los comentarios principales
     * @param page número de pagina sobre el que listar comentarios
     * @param size tamaño de elementos de cada página listada
     */
    suspend fun getMainCommentsFromPost(
        id: Long,
        page: Int,
        size: Int
    ): APIResponse<PageableObject<CommentResponseItem>>{
        return try{
            val response = commentServiceAuth.getPublicationMainComments(id, page, size)
            if(response.isSuccessful){
                response.body().let { commentList ->
                    APIResponse(
                        success = commentList?.success ?: true,
                        message = commentList?.message ?: R.string.general_response.toString(),
                        objectResponse = commentList?.objectResponse
                    )
                }
            }else {
                response.errorBody().let {responseError ->
                    val errorResponse = Gson().fromJson(responseError?.string(), APIResponse::class.java)
                    APIResponse(
                        success = errorResponse?.success ?: false,
                        message = errorResponse?.message ?: R.string.general_error.toString(),
                        objectResponse = null
                    )
                }
            }
        } catch (e: SocketTimeoutException) {
            APIResponse(
                success = null,
                message = context.getString(R.string.general_timeout),
                objectResponse = null
            )

        } catch (e: Exception) {
            APIResponse(
                success = null,
                message = context.getString(R.string.unknown_error),
                objectResponse = null
            )
        }
    }

    /**
     * Función que realiza una llamada al servidor para devolver un objeto <PageableObject> que contiene un listado de <CommentResponseItem>
     * con los comentarios que son una respuesta a un comentario pasado como <commentId>
     * @param commentId identificador del comentario sobre el que se quieren obtener las respuestas directas
     * @param page número de página sobre el que listar comentarios
     * @param size tamaño de elementos de cada página listada.
     */
    suspend fun loadRepliesFromComment(
        commentId : Long,
        page : Int,
        size : Int) : APIResponse<PageableObject<CommentResponseItem>>{
            return try {
                val response = commentServiceNoAuth.getDirectRepliesFromComment(commentId, page, size)
                if(response.isSuccessful){
                    response.body().let { commentList ->
                        APIResponse(
                            success = commentList?.success ?: true,
                            message = commentList?.message ?: R.string.general_response.toString(),
                            objectResponse = commentList?.objectResponse
                        )
                    }
                }else {
                    response.errorBody().let {responseError ->
                        val errorResponse = Gson().fromJson(responseError?.string(), APIResponse::class.java)
                        APIResponse(
                            success = errorResponse?.success ?: false,
                            message = errorResponse?.message ?: R.string.general_error.toString(),
                            objectResponse = null
                        )
                    }
                }
            }catch (e: SocketTimeoutException) {
                APIResponse(
                    success = null,
                    message = context.getString(R.string.general_timeout),
                    objectResponse = null
                )

            } catch (e: Exception) {
                APIResponse(
                    success = null,
                    message = context.getString(R.string.unknown_error),
                    objectResponse = null
                )
            }

    }

    /**
     * Función que realiza una llamada al servidor para introducir un nuevo comentario <commentRequestItem> en la base de datos
     * @param id identificador del post en el que se añadirá el comentario
     * @param commentRequestItem datos del comentario a añadir
     * @return El servidor devolverá un <CommentResponseItem> con los datos del comentario añadido
     */
    suspend fun createComment(
        id : Long,
        commentRequestItem: CommentRequestItem
    ) : APIResponse<CommentResponseItem?> {
        return try{
            val response = commentServiceAuth.createComment(id, commentRequestItem)
            Log.i("comments", "comment upload => " + response.toString())
            if(response.isSuccessful){
                response.body().let { comment ->
                    APIResponse(
                        success = comment?.success ?: true,
                        message = comment?.message ?: R.string.general_response.toString(),
                        objectResponse = comment?.objectResponse
                    )
                }
            }else {
                response.errorBody().let {responseError ->
                    val errorResponse = Gson().fromJson(responseError?.string(), APIResponse::class.java)
                    APIResponse(
                        success = errorResponse?.success ?: false,
                        message = errorResponse?.message ?: R.string.general_error.toString(),
                        objectResponse = null
                    )
                }
            }
        }catch (e: SocketTimeoutException) {
            APIResponse(
                success = null,
                message = context.getString(R.string.general_timeout),
                objectResponse = null
            )

        } catch (e: Exception) {
            APIResponse(
                success = null,
                message = context.getString(R.string.unknown_error),
                objectResponse = null
            )
        }
    }

    /**
     * Función que realiza una llamada al servidor para eliminar un comentario pasado como <id>
     * Para poder eliminar un comentario es necesario ser el usuario propietario o un usuario con roles de ADMIN
     * La validación de estos datos se realiza en el propio servidor
     * @param id  identificador del comentario a eliminar
     * @return <APIResponse> cuyo <ObjectResponse> es null
     */
    suspend fun deleteCommentById(
        id : Long
    ): APIResponse<Unit>{
        return try{
            val response = commentServiceAuth.deleteCommentById(id)
            if(response.isSuccessful){
                response.body().let { comment ->
                    APIResponse(
                        success = comment?.success ?: true,
                        message = comment?.message ?: R.string.general_response.toString(),
                        objectResponse = comment?.objectResponse
                    )
                }
            }else {
                response.errorBody().let {responseError ->
                    val errorResponse = Gson().fromJson(responseError?.string(), APIResponse::class.java)
                    APIResponse(
                        success = errorResponse?.success ?: false,
                        message = errorResponse?.message ?: R.string.general_error.toString(),
                        objectResponse = null
                    )
                }
            }
        }catch (e: SocketTimeoutException) {
            APIResponse(
                success = null,
                message = context.getString(R.string.general_timeout),
                objectResponse = null
            )

        } catch (e: Exception) {
            APIResponse(
                success = null,
                message = context.getString(R.string.unknown_error),
                objectResponse = null
            )
        }
    }

}