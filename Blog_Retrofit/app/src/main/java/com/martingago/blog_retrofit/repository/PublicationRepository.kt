package com.martingago.blog_retrofit.repository

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.martingago.blog_retrofit.R
import com.martingago.blog_retrofit.model.APIResponse
import com.martingago.blog_retrofit.model.pageable.PageableObject
import com.martingago.blog_retrofit.model.post.PostRequestItem
import com.martingago.blog_retrofit.model.post.PublicacionResponseItem
import com.martingago.blog_retrofit.network.ApiClient
import com.martingago.blog_retrofit.service.PublicationService
import java.net.SocketTimeoutException


class PublicationRepository(private val context: Context){

    private val publicationServiceNoAuth: PublicationService
    private val publicationServiceAuth: PublicationService

    init {
        // Inicialización de servicios con y sin autenticación
        publicationServiceNoAuth = ApiClient.getInstanceWithoutAuth().create(PublicationService::class.java)
        publicationServiceAuth = ApiClient.getInstanceWithAuth(context).create(PublicationService::class.java)
    }

    /**
     * Funcion que realiza una llamada al servidor para añadir una nueva publicación
     * @param postRequestItem datos con la nueva publicación a añadir
     * @return APIResponse<PublicacionResponseItem> datos de la publicación añadidos en el servidor
     */
    suspend fun addNewPublication(
        postRequestItem: PostRequestItem
    ) : APIResponse<PublicacionResponseItem?>{
        return try{
            val response = publicationServiceAuth.addNewPost(postRequestItem)
            Log.e("post", "publicando post: "+response.toString())
            if (response.isSuccessful) {
                response.body().let { publication ->
                    APIResponse(
                        success = publication?.success ?: true,
                        message = publication?.message ?: R.string.general_response.toString(),
                        objectResponse = publication?.objectResponse
                    )
                }
            } else {
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
     * función que realiza una llamada al API para eliminar una publicación pasada como ID
     * @param id identificador del post a eliminar
     */
    suspend fun deletePostById(
        id : Long
    ): APIResponse<Unit>{
        return try{
            val response = publicationServiceAuth.deletePostById(id)
            if (response.isSuccessful) {
                response.body().let { publications ->
                    APIResponse(
                        success = publications?.success ?: true,
                        message = publications?.message ?: R.string.general_response.toString(),
                        objectResponse = publications?.objectResponse
                    )
                }
            } else {
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
     * Función que realiza una llamada al servidor para actualizar los datos de una publicación
     * @param id identificador del post a actualizar
     * @param publicationRequestItem datos actualizados de la publicación
     */
    suspend fun updatePublicationById(
        id : Long,
        publicationRequestItem: PostRequestItem
    ) : APIResponse<PublicacionResponseItem>{
        return try{
            val response = publicationServiceAuth.updatePostById(id, publicationRequestItem)
            if (response.isSuccessful) {
                response.body().let { publications ->
                    APIResponse(
                        success = publications?.success ?: true,
                        message = publications?.message ?: R.string.general_response.toString(),
                        objectResponse = publications?.objectResponse
                    )
                }
            } else {
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
     * Función que realiza una llamada para listar las publicaciones existentes
     * @param order orden para mostrar las publicaciones: asc, desc.
     * @param field campo por el que ordenar los resultados
     * @param page número de pagina
     * @param size tamaño de la página (máximo 24 manejado desde el servidor)
     */
    suspend fun fetchPublications(
        order: String,
        field :String,
        page: Int,
        size: Int,
    ): APIResponse<PageableObject<PublicacionResponseItem>> {
            return try {
                val response = publicationServiceNoAuth.getPaginationPublications(order, field, page, size)
                if (response.isSuccessful) {
                    response.body().let { publications ->
                        APIResponse(
                            success = publications?.success ?: true,
                            message = publications?.message ?: R.string.general_response.toString(),
                            objectResponse = publications?.objectResponse
                        )
                    }
                } else {
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