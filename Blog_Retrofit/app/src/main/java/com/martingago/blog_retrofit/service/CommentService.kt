package com.martingago.blog_retrofit.service

import com.martingago.blog_retrofit.model.APIResponse
import com.martingago.blog_retrofit.model.comment.CommentRequestItem
import com.martingago.blog_retrofit.model.comment.CommentResponseItem
import com.martingago.blog_retrofit.model.pageable.PageableObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface CommentService {
    @GET("public/post/{id}/comments")
    suspend fun getPublicationMainComments(
        @Path("id")id : Long,
        @Query("page") page : Int,
        @Query("size") size : Int
    ) : Response<APIResponse<PageableObject<CommentResponseItem>>>

    @GET("public/comments/{id}/replies")
    suspend fun getDirectRepliesFromComment(
        @Path("id") id : Long,
        @Query("page") page: Int,
        @Query("size") size : Int
    ) : Response<APIResponse<PageableObject<CommentResponseItem>>>

    @POST("user/post/{id}/comment")
    suspend fun createComment(
        @Path("id") id : Long,
        @Body commentRequestItem: CommentRequestItem
    ) : Response<APIResponse<CommentResponseItem?>>

    @DELETE("user/comment/{id}")
    suspend fun deleteCommentById(
        @Path("id") id : Long
    ): Response<APIResponse<Unit>>

}