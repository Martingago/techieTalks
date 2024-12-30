package com.martingago.blog_retrofit.service

import com.martingago.blog_retrofit.model.APIResponse
import com.martingago.blog_retrofit.model.pageable.PageableObject
import com.martingago.blog_retrofit.model.post.PostRequestItem
import com.martingago.blog_retrofit.model.post.PostResponseItem
import com.martingago.blog_retrofit.model.post.PublicacionResponseItem
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface PublicationService {
    @GET("public/post/{id}")
    suspend fun getPostById(
        @Path("id") id: Long
    ): Response<APIResponse<PublicacionResponseItem>>

    @GET("public/post")
    suspend fun getPaginationPublications(
        @Query("order") order: String,
        @Query("field") field : String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<APIResponse<PageableObject<PublicacionResponseItem>>>

    @POST("editor/post")
    suspend fun addNewPost(
        @Body postRequestItem: PostRequestItem
    ) : Response<APIResponse<PublicacionResponseItem>>

    @PUT("editor/post/{id}")
    suspend fun updatePostById(
        @Path("id") id : Long,
        @Body postRequestItem: PostRequestItem
    ) : Response<APIResponse<PublicacionResponseItem>>

    @DELETE("admin/post/{id}")
    suspend fun deletePostById(
        @Path("id") id : Long
    ) : Response<APIResponse<Unit>>
}