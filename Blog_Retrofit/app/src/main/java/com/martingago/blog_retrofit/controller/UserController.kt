package com.martingago.blog_retrofit.controller

import android.content.Context
import com.martingago.blog_retrofit.model.APIResponse
import com.martingago.blog_retrofit.model.pageable.PageableObject
import com.martingago.blog_retrofit.model.roles.RolesRequestItem
import com.martingago.blog_retrofit.model.user.UserRegisterRequest
import com.martingago.blog_retrofit.model.user.UserRequest
import com.martingago.blog_retrofit.model.user.UserResponseItem
import com.martingago.blog_retrofit.model.user.UserUpdate
import com.martingago.blog_retrofit.network.ApiCallsUtil
import com.martingago.blog_retrofit.repository.UserRepository

class UserController (private val context: Context){

    //Inicialización del Repository
    private val userRepository = UserRepository(context)

    /**
     * Controller que realiza una petición al servidor para validar los datos de authenticación de un usuario <UserRequest> que quiere iniciar sesión
     * Maneja las llamadas realizadas por los Repositories gestionando el número de intentos que realizará cada petición hasta devolver un error a través del ApiCallsUtils
     */
    suspend fun loginUser(
        userRequest: UserRequest,
        updateStatus: (currentAttempt: Int, maxAttempts: Int) -> Unit = { _, _ -> },
    ): APIResponse<UserResponseItem?>{
        return ApiCallsUtil.callAPIWithRetry(updateStatus = updateStatus) {
            userRepository.loginUser(userRequest)
        } as APIResponse<UserResponseItem?>
    }


    /**
     * Controller que maneja la creación de un nuevo usuario en el servidor <UserRegisterRequest>
     * El servidor devolverá un <UserResponse> con los datos básicos del usuario registrado
     */
    suspend fun registerUser(
        userRegisterRequest: UserRegisterRequest,
        updateStatus: (currentAttempt: Int, maxAttempts: Int) -> Unit = { _, _ -> }
    ) : APIResponse<UserResponseItem>{
        return ApiCallsUtil.callAPIWithRetry (updateStatus = updateStatus){
            userRepository.registerUser(userRegisterRequest)
        } as APIResponse<UserResponseItem>
    }

    /**
     * Controller que maneja la solicitud de los datos de un usuario en el servidor
     * El servidor devolverá un objeto <UserResponse> si el id proporcionado se encuentra en la base de datos.
     */
    suspend fun getUserProfileById(
        id: Long,
        updateStatus: (currentAttempt: Int, maxAttempts: Int) -> Unit = { _, _ -> }
    ) : APIResponse<UserResponseItem?>{
        return ApiCallsUtil.callAPIWithRetry(updateStatus = updateStatus) {
            userRepository.getUserProfileById(id)
        } as APIResponse<UserResponseItem?>
    }

    /**
     * Controller que maneja la obtención de un <PageableObject> que contiene los <UserResponseItem> extraidos de la base de datos
     */
    suspend fun getPageableUsers(
        order : String,
        field : String,
        page : Int,
        size : Int,
        updateStatus: (currentAttempt: Int, maxAttempts: Int) -> Unit = { _, _ -> }
    ): APIResponse<PageableObject<UserResponseItem>>{
        return  ApiCallsUtil.callAPIWithRetry(updateStatus = updateStatus) {
            userRepository.getPageableUsers(
                order,
                field,
                page,
                size)
        } as APIResponse<PageableObject<UserResponseItem>>
    }

    /**
     * Controller que actualiza los roles de un usuario en el servidor.
     * Sólo usuarios con roles de <ADMIN> pueden actualizar los roles de otros usuarios
     */
    suspend fun updateUserRoles(
        id: Long,
        rolesRequestItem: RolesRequestItem,
        updateStatus: (currentAttempt: Int, maxAttempts: Int) -> Unit = { _, _ -> }
    ) : APIResponse<UserResponseItem>{
        return ApiCallsUtil.callAPIWithRetry(updateStatus = updateStatus ) {
            userRepository.updateUserRoles(id, rolesRequestItem)
        } as APIResponse<UserResponseItem>
    }

    /**
     * Controller que maneja la actualización de un usuario en el servidor a través de un objeto <userUpdate>
     * que maneja los datos que pueden ser actualizados de un usuario de la aplicación.
     * El servidor devolverá un <UserResponse> que contendrá los datos actualizados en la aplicación del usuario
     */
    suspend fun updateUserDetailsById(
        id : Long,
        userUpdate: UserUpdate,
        updateStatus: (currentAttempt: Int, maxAttempts: Int) -> Unit = { _, _ -> }
    ) : APIResponse<UserResponseItem>{
        return ApiCallsUtil.callAPIWithRetry(updateStatus = updateStatus) {
            userRepository.updateUserDetails(id, userUpdate)
        } as APIResponse<UserResponseItem>
    }

    /**
     * Controller que maneja la eliminación de un usuario en la base de datos a través de un <id>
     * Para poder eliminar con éxito un usuario es necesario ser el propio usuario a eliminar o una cuenta con roles de ADMIN
     * El servidor devolverá un objeto APIResponse cuyo <ObjectResponse> será null <Unit>
     */
    suspend fun deleteUserById(
        id : Long,
        updateStatus: (currentAttempt: Int, maxAttempts: Int) -> Unit = { _, _ -> }
    ) : APIResponse<Unit>{
        return ApiCallsUtil.callAPIWithRetry(updateStatus = updateStatus) {
            userRepository.deleteUserById(id)
        } as APIResponse<Unit>
    }

}