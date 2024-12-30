package com.martingago.blog_retrofit.repository

import android.content.Context
import com.google.gson.Gson
import com.martingago.blog_retrofit.R
import com.martingago.blog_retrofit.model.APIResponse
import com.martingago.blog_retrofit.model.pageable.PageableObject
import com.martingago.blog_retrofit.model.tag.TagRequestItem
import com.martingago.blog_retrofit.model.tag.TagResponseItem
import com.martingago.blog_retrofit.network.ApiClient
import com.martingago.blog_retrofit.service.TagService
import java.net.SocketTimeoutException

class TagRepository (private val context: Context){
    private val tagServiceNoAuth : TagService
    private val tagServiceWithAuth : TagService

    init {
        tagServiceNoAuth = ApiClient.getInstanceWithoutAuth().create(TagService::class.java)
        tagServiceWithAuth = ApiClient.getInstanceWithAuth(context).create(TagService::class.java)
    }

    /**
     * Función que realiza una petición al servidor solicitando un objeto pageable de <TagResponseItems>
     * @param page número de página que se quiere solicitar del objeto pageable
     * @param size tamaño de elementos de página que se quiere solicitar (máximo 24)
     */
    suspend fun fetchPaginationTags(
        page: Int,
        size: Int
    ): APIResponse<PageableObject<TagResponseItem>?> {
        return try {
            val response = tagServiceNoAuth.getTagsPageable(page, size)
            if (response.isSuccessful) {
                response.body().let { dataTags ->
                    APIResponse(
                        success = dataTags?.success ?: true,
                        message = dataTags?.message ?: R.string.general_response.toString(),
                        objectResponse = dataTags?.objectResponse
                    )
                }
            } else {
                response.errorBody().let { dataErrorTags ->
                    val responseError =
                        Gson().fromJson(dataErrorTags?.string(), APIResponse::class.java)
                    APIResponse(
                        success = responseError?.success ?: false,
                        message = responseError?.message ?: R.string.general_error.toString(),
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
     * Función que realiza una petición al servidor añadiendo una tag a la base de datos.
     * @param tagRequestItem datos de la etiqueta a añadir
     * Para poder añadir una tag correctamente es necesario tener roles de ADMIN.
     * La validación de este rol se realizará en el propio servidor mediante un Middleware.
     */
    suspend fun addNewTag(
        tagRequestItem: TagRequestItem
    ): APIResponse<TagResponseItem?> {
        return try{
            val response = tagServiceWithAuth.addTag(tagRequestItem)
            if (response.isSuccessful) {
                response.body().let { dataTags ->
                    APIResponse(
                        success = dataTags?.success ?: true,
                        message = dataTags?.message ?: R.string.general_response.toString(),
                        objectResponse = dataTags?.objectResponse
                    )
                }
            } else {
                response.errorBody().let { dataErrorTags ->
                    val responseError =
                        Gson().fromJson(dataErrorTags?.string(), APIResponse::class.java)
                    APIResponse(
                        success = responseError?.success ?: false,
                        message = responseError?.message ?: R.string.general_error.toString(),
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
     * Función que realiza una petición al servidor para eliminar una tag de la base de datos
     * @param id identificador de la tag que se quiere eliminar de la base de datos
     * Es necesario tener roles de ADMIN para eliminar una Tag de la base de datos
     */
    suspend fun deleteTagById(
        id : Long
    ) : APIResponse<Unit>{
        return try{
            val response = tagServiceWithAuth.deleteTagById(id)
            if(response.isSuccessful){
                response.body().let { dataTags ->
                    APIResponse(
                        success = dataTags?.success ?: true,
                        message = dataTags?.message ?: R.string.general_response.toString(),
                        objectResponse = dataTags?.objectResponse
                    )
                }
            } else {
                response.errorBody().let { dataErrorTags ->
                    val responseError =
                        Gson().fromJson(dataErrorTags?.string(), APIResponse::class.java)
                    APIResponse(
                        success = responseError?.success ?: false,
                        message = responseError?.message ?: R.string.general_error.toString(),
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
     * Función que realiza una petición al servidor editando una tag en la base de datos.
     * @param id identificador de la tag a editar
     * @param tagRequestItem objeto que recibirá el servidor con los datos actualizados de la tag
     * Es necesario tener roles de ADMIN para editar una tag en la base de datos.
     */
    suspend fun updateTagById(
        id : Long,
        tagRequestItem: TagRequestItem
    ) : APIResponse<TagResponseItem?>{
        return try {
            val response = tagServiceWithAuth.updateTag(id, tagRequestItem)
            if(response.isSuccessful){
                response.body().let { dataTags ->
                    APIResponse(
                        success = dataTags?.success ?: true,
                        message = dataTags?.message ?: R.string.general_response.toString(),
                        objectResponse = dataTags?.objectResponse
                    )
                }
            } else {
                response.errorBody().let { dataErrorTags ->
                    val responseError =
                        Gson().fromJson(dataErrorTags?.string(), APIResponse::class.java)
                    APIResponse(
                        success = responseError?.success ?: false,
                        message = responseError?.message ?: R.string.general_error.toString(),
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
}