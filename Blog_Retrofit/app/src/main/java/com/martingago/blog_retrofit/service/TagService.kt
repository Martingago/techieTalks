package com.martingago.blog_retrofit.service

import com.martingago.blog_retrofit.model.APIResponse
import com.martingago.blog_retrofit.model.pageable.PageableObject
import com.martingago.blog_retrofit.model.tag.TagRequestItem
import com.martingago.blog_retrofit.model.tag.TagResponseItem
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface TagService {

    //Obtiene un objeto pageable con el listado de <TagResponseItem>
    @GET("public/tags")
    suspend fun getTagsPageable(
        @Query("page") page : Int,
        @Query("size") size : Int
    ) : Response<APIResponse<PageableObject<TagResponseItem>>>

    // Añade una tag y devuelve el objeto <TagResponseItem> creado en la Base de datos
    @POST("admin/tags")
    suspend fun  addTag(
        @Body tagRequestItem: TagRequestItem
    ) : Response<APIResponse<TagResponseItem>>

    //Actualiza los datos de una Tag
    @PUT("admin/tags/{id}")
    suspend fun updateTag(
        @Path("id") id : Long,
        @Body tagRequestItem: TagRequestItem
    ) : Response<APIResponse<TagResponseItem>>

    //Elimina una tag de la base de datos: El objeto eliminado se devolverá como null <Unit>
    @DELETE("admin/tags/{id}")
    suspend fun deleteTagById(
        @Path("id") id : Long
    ) : Response<APIResponse<Unit>>
}