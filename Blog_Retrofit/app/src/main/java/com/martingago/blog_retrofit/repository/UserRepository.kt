package com.martingago.blog_retrofit.repository

import android.content.Context
import com.google.gson.Gson
import com.martingago.blog_retrofit.R
import com.martingago.blog_retrofit.model.APIResponse
import com.martingago.blog_retrofit.model.pageable.PageableObject
import com.martingago.blog_retrofit.model.roles.RolesRequestItem
import com.martingago.blog_retrofit.model.user.UserRegisterRequest
import com.martingago.blog_retrofit.model.user.UserRequest
import com.martingago.blog_retrofit.model.user.UserResponseItem
import com.martingago.blog_retrofit.model.user.UserUpdate
import com.martingago.blog_retrofit.network.ApiClient
import com.martingago.blog_retrofit.service.UserService
import java.net.SocketTimeoutException

class UserRepository (private val context: Context){

    private val userServiceNoAuth : UserService
    private var userServiceWithAuth : UserService

    init {
    userServiceWithAuth = ApiClient.getInstanceWithAuth(context).create(UserService::class.java)
    userServiceNoAuth = ApiClient.getInstanceWithoutAuth().create(UserService::class.java)

    }

    /**
     * Función que emplea el Service de Usuario sin authenticación previa para realizar el login del usuario en la aplicación
     * @param userRequest DTO del usuario para validar en el servidor e iniciar sesión
     */
    suspend fun loginUser(
        userRequest: UserRequest,
        ): APIResponse<UserResponseItem?>{
        return try {
            val response = userServiceNoAuth.login(userRequest)
            if(response.isSuccessful){
                response.body().let { dataUser ->
                    APIResponse(
                        success = dataUser?.success ?: true,
                        message = dataUser?.message ?: R.string.general_response.toString(),
                        objectResponse = dataUser?.objectResponse
                    )
                }
            }else{
                response.errorBody().let { dataUser ->
                    val responseError = Gson().fromJson(dataUser?.string(), APIResponse::class.java)
                    APIResponse(
                        success = responseError?.success ?: false,
                        message = responseError?.message ?: R.string.general_error.toString(),
                        objectResponse = null
                    )
                }
            }
        }catch (e : SocketTimeoutException){
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
     * Función que registra un nuevo usuario en la base de datos.
     * @param userRegisterRequest objeto que maneja los datos de registro del usuario
     * @return APIResponse<UserResponse?> si la llamada es correcta se devolverá un APIResponse que contendrá un <UserResponse> con los datos almacenados en el servidor
     */
    suspend fun registerUser(
        userRegisterRequest: UserRegisterRequest
    ) : APIResponse<UserResponseItem?>{
        return try {
            val response = userServiceWithAuth.registerUser(userRegisterRequest)
            if(response.isSuccessful){
                response.body().let { responseUser ->
                    APIResponse(
                        success =  responseUser?.success ?: true,
                        message = responseUser?.message ?: R.string.general_response.toString(),
                        objectResponse =  responseUser?.objectResponse
                    )
                }
            }else{
                response.errorBody().let { dataUser ->
                    val responseError = Gson().fromJson(dataUser?.string(), APIResponse::class.java)
                    APIResponse(
                        success = responseError?.success ?: false,
                        message = responseError?.message ?: R.string.general_error.toString(),
                        objectResponse = null
                    )
                }
            }
        }catch (e : SocketTimeoutException){
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
     * Función que obtiene los datos de perfil de un usuario de la base de datos
     * @param id identificador del usuario sobre el que se quiere obtener los datos
     * @return APIResponse<UserResponse> con los datos del usuario solicitado de la base de datos.
     */
    suspend fun getUserProfileById(
        id : Long
    ) : APIResponse<UserResponseItem?>{
        return try{
            val response = userServiceNoAuth.getUserProfile(id)
            if(response.isSuccessful){
                response.body().let { responseUser ->
                    APIResponse(
                        success =  responseUser?.success ?: true,
                        message = responseUser?.message ?: R.string.general_response.toString(),
                        objectResponse =  responseUser?.objectResponse
                    )
                }
            }else {
                response.errorBody().let { dataUser ->
                    val responseError = Gson().fromJson(dataUser?.string(), APIResponse::class.java)
                    APIResponse(
                        success = responseError?.success ?: false,
                        message = responseError?.message ?: R.string.general_error.toString(),
                        objectResponse = null
                    )
                }
            }
        }catch (e : SocketTimeoutException){
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
     * Función que actualiza los datos de un usuario en la base de datos.
     * @param id identificador del usuario sobre el que se quieren actualizar los datos
     * @param userUpdate DTO especifico que contiene los atributos que pueden ser actualizables de un usuario
     * @return APIResponse<UserResponse> objeto APIResponse que contiene un <UserResponse> con los datos actualizados del usuario
     */
    suspend fun updateUserDetails(
        id : Long,
        userUpdate: UserUpdate
    ) : APIResponse<UserResponseItem?>{
        return try{
            val response = userServiceWithAuth.updateUser(id, userUpdate)
            if(response.isSuccessful){
                response.body().let { responseUser ->
                    APIResponse(
                        success =  responseUser?.success ?: true,
                        message = responseUser?.message ?: R.string.general_response.toString(),
                        objectResponse =  responseUser?.objectResponse
                    )
                }
            }else{
                response.errorBody().let { dataUser ->
                    val responseError = Gson().fromJson(dataUser?.string(), APIResponse::class.java)
                    APIResponse(
                        success = responseError?.success ?: false,
                        message = responseError?.message ?: R.string.general_error.toString(),
                        objectResponse = null
                    )
                }
            }

        }catch (e : SocketTimeoutException){
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
     * Función que elimina un usuario de la base de datos.
     * Para poder eliminar un usuario es necesario ser el propio usuario a eliminar o tener roles de ADMIN.
     * La validación de estos datos se realiza en el propio servidor.
     * @param id identificador del usuario a eliminar
     * @return El servidor devolverá un APIResponse cuyo <ObjectResponse> será null <Unit>
     */
    suspend fun deleteUserById(
        id : Long) : APIResponse<Unit>{
        return try{
            val response = userServiceWithAuth.deleteUserById(id)
            if(response.isSuccessful){
                response.body().let { responseUser ->
                    APIResponse(
                        success =  responseUser?.success ?: true,
                        message = responseUser?.message ?: R.string.general_response.toString(),
                        objectResponse =  responseUser?.objectResponse
                    )
                }
            }else{
                response.errorBody().let { dataUser ->
                    val responseError = Gson().fromJson(dataUser?.string(), APIResponse::class.java)
                    APIResponse(
                        success = responseError?.success ?: false,
                        message = responseError?.message ?: R.string.general_error.toString(),
                        objectResponse = null
                    )
                }
            }

        }catch (e : SocketTimeoutException){
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
     * Función que actualiza los roles de un usuario pasado como id
     * @param id identificador del usuario sobre el que actualizar sus roles
     * @param rolesRequestItem List con los id de los nuevos roles que tendrá el usuario
     */
    suspend fun  updateUserRoles(
        id : Long,
        rolesRequestItem: RolesRequestItem
    ) : APIResponse<UserResponseItem>{
        return try {
            val response = userServiceWithAuth.updateUserRoles(id, rolesRequestItem)
            if(response.isSuccessful){
                response.body().let { responseUser ->
                    APIResponse(
                        success =  responseUser?.success ?: true,
                        message = responseUser?.message ?: R.string.general_response.toString(),
                        objectResponse =  responseUser?.objectResponse
                    )
                }
            }else{
                response.errorBody().let { dataUser ->
                    val responseError = Gson().fromJson(dataUser?.string(), APIResponse::class.java)
                    APIResponse(
                        success = responseError?.success ?: false,
                        message = responseError?.message ?: R.string.general_error.toString(),
                        objectResponse = null
                    )
                }
            }
        }catch (e : SocketTimeoutException){
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
     * Función que obtiene de la base de datos un objeto Pageable que contiene en su interior un conjunto de <UserResponse> paginados
     * @param order orden en el que se muestran los elementos: asc / desc
     * @param field atributo del objeto sobre el que se realiza la ordenación (Default ID)
     * @param page número de página a mostrar
     * @param size Número de elementos por página a mostrar
     */
    suspend fun getPageableUsers(
        order : String,
        field : String,
        page : Int,
        size : Int
    ): APIResponse<PageableObject<UserResponseItem>>{
        return try {
            val response = userServiceWithAuth.getPaginatedUsers(order, field,page,size)
            if(response.isSuccessful){
                response.body().let { responseUser ->
                    APIResponse(
                        success =  responseUser?.success ?: true,
                        message = responseUser?.message ?: R.string.general_response.toString(),
                        objectResponse =  responseUser?.objectResponse
                    )
                }
            }else{
                response.errorBody().let { dataUser ->
                    val responseError = Gson().fromJson(dataUser?.string(), APIResponse::class.java)
                    APIResponse(
                        success = responseError?.success ?: false,
                        message = responseError?.message ?: R.string.general_error.toString(),
                        objectResponse = null
                    )
                }
            }

        }catch (e : SocketTimeoutException){
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