package com.martingago.blog_retrofit.service

import com.martingago.blog_retrofit.model.APIResponse
import com.martingago.blog_retrofit.model.pageable.PageableObject
import com.martingago.blog_retrofit.model.roles.RolesRequestItem
import com.martingago.blog_retrofit.model.user.UserRegisterRequest
import com.martingago.blog_retrofit.model.user.UserRequest
import com.martingago.blog_retrofit.model.user.UserResponseItem
import com.martingago.blog_retrofit.model.user.UserUpdate
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface UserService {

    //Inicia la sesión de un usuario
    @POST("public/login")
    @Headers("Content-Type: application/json")
    suspend fun login(
        @Body userRequest: UserRequest
    ) : Response<APIResponse<UserResponseItem>>

    //Registra un nuevo usuario en la base de datos
    @POST("register/user")
    suspend fun registerUser(
        @Body userRegisterRequest: UserRegisterRequest
    ) : Response<APIResponse<UserResponseItem>>

    //Actualiza el perfil de un usuario en la base de datos
    @PUT("user/profile/{id}")
    suspend fun updateUser(
        @Path("id") id: Long,
        @Body userUpdate: UserUpdate
    ) : Response<APIResponse<UserResponseItem>>

    //Obtiene el perfil de un usuario desde la base de datos
    @GET("public/profile/{id}")
    suspend fun getUserProfile(
        @Path("id") id :Long
    ) : Response<APIResponse<UserResponseItem>>

    //Elimina un usuario de la base de datos
    @DELETE("user/profile/{id}")
    suspend fun deleteUserById(
        @Path("id") id : Long
    ) : Response<APIResponse<Unit>>

    @PUT("user/profile/{id}/roles")
    suspend fun updateUserRoles(
        @Path("id") id : Long,
        @Body rolesRequestItem: RolesRequestItem
    ) : Response<APIResponse<UserResponseItem>>

    // Obtiene un objeto Page con un listado de usuarios procedentes de la base de datos
    @GET("admin/users")
    suspend fun getPaginatedUsers(
        @Query("order") order : String,
        @Query("field") field : String,
        @Query("page") page : Int,
        @Query("size") size : Int
    ) : Response<APIResponse<PageableObject<UserResponseItem>>>
}