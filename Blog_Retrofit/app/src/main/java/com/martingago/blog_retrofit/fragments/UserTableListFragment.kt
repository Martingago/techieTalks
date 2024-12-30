package com.martingago.blog_retrofit.fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.martingago.blog_retrofit.R
import com.martingago.blog_retrofit.adapter.UserTableAdapter
import com.martingago.blog_retrofit.controller.UserController
import com.martingago.blog_retrofit.model.user.UserResponseItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class UserTableListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var userAdapter: UserTableAdapter
    private lateinit var userController: UserController

    private lateinit var loadingArea : View
    private lateinit var loadingText : TextView

    private val users = mutableListOf<UserResponseItem>()

    private var isLoading = false
    private var currentPage = 0
    private var totalPages = 1 //Se inicializa en 1 y luego se actualiza con su valor real

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_user_table_list, container, false)
        userController = UserController(requireContext())
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.usersRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        loadingArea = view.findViewById(R.id.layoutLoadingArea)
        loadingText = view.findViewById(R.id.loadingText)

        // Inicializar el adaptador vacío
        userAdapter = UserTableAdapter(
            users = users, // Lista vacía por ahora
            onEditClick = { user -> editUser(user) },
            onDeleteClick = { user -> deleteUser(user) }
        )
        recyclerView.adapter = userAdapter
        loadUsers()
        initRecyclerView()
    }

    private fun initRecyclerView(){
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                if (!isLoading && currentPage < totalPages) {
                    if (visibleItemCount + firstVisibleItemPosition >= totalItemCount &&
                        firstVisibleItemPosition >= 0
                    ) {
                        loadMoreUsers()
                    }
                }
            }
        })
    }

    private  fun editUser(userResponseItem: UserResponseItem){
        findNavController().navigate(SectionManageUsersFragmentDirections.actionSectionManageUsersFragmentToUpdateUserRolesFragment(userResponseItem))

    }

    private fun deleteUser(userResponseItem: UserResponseItem){
        showDeleteConfirmationDialog(userResponseItem)
    }

    private fun loadMoreUsers() {
        if (currentPage < totalPages) {
            currentPage++
            loadUsers(append = true)
        }
    }

    private fun loadUsers(append : Boolean = false) {
        if (!append) {
            currentPage = 0
            totalPages = 1
            users.clear()
            userAdapter.notifyDataSetChanged()
        }

        isLoading = true

        lifecycleScope.launch(Dispatchers.Main) {
            try {
                // Llamada a la API para obtener el listado de usuarios
                val response = withContext(Dispatchers.IO) {
                    userController.getPageableUsers(
                        order = "desc",
                        field = "id",
                        page = currentPage,
                        size = 24, //
                        updateStatus = { currentAttempt, maxAttempts ->
                            showLoader(currentAttempt, maxAttempts) // Actualizar el estado en la UI en cada intento
                        }
                    )
                }
                if (response.success == true && response.objectResponse?.content != null) {
                    response.objectResponse.let { userResponse ->
                        // Actualizar el adaptador con las publicaciones obtenidas
                        totalPages = userResponse.totalPages // Se actualiza el total de páginas
                        val newUsers = userResponse.content
                        users.addAll(newUsers)
                        withContext(Dispatchers.Main) {
                            if (isAdded) userAdapter.notifyDataSetChanged()
                        }

                    }
                } else {
                    showError(getString(R.string.error_loading_users))
                }
                hideLoader()
            } catch (e: Exception) {
                showError(getString(R.string.error_loading_users))
            }finally {
                hideLoader()
                isLoading = false
            }
        }
    }

    /**
     * Función que muestra un Dialog de confirmación cuando el usuario quiere eliminar un usuario.
     * Además también muestra un loader durante el proceso de eliminación de datos.
     * Una vez eliminado el usuario, se actualiza la lista local de usuarios.
     */
    private fun showDeleteConfirmationDialog(userResponseItem: UserResponseItem) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.delete_user))
        builder.setMessage(getString(R.string.message_confirm_delete_user, userResponseItem.username))

        // Código para el boton de confirmar, se llama al controller que gestiona eliminación de publicaciones y se muestra un loader
        builder.setPositiveButton("Sí") { dialog, _ ->
            lifecycleScope.launch(Dispatchers.Main) {
                try {
                    //Muestra el dialog parametrizado
                    showLoaderDelete(userResponseItem.username)
                    val response = withContext(Dispatchers.IO) {
                        userController.deleteUserById(userResponseItem.id)
                    }
                    //Muestra mensaje personalizado en base a la respuesta del servidor
                    if (response.success == true) {
                        userAdapter.deleteLocalUserById(userResponseItem.id)
                        Toast.makeText(requireContext(), getString(R.string.delete_user_confirm), Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), getString(R.string.delete_user_error), Toast.LENGTH_SHORT).show()
                    }
                    hideLoader()
                } catch (e: Exception) {
                    showError(getString(R.string.delete_user_error))
                }
            }
            dialog.dismiss()
        }

        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss() // Cerrar el diálogo
        }
        // Mostrar el diálogo
        builder.create().show()
    }

    private fun hideLoader(){
        loadingArea.visibility= View.GONE
        recyclerView.visibility= View.VISIBLE
    }
    private fun showLoader(currentAttempt : Int, maxAttempt: Int){
        loadingArea.visibility = View.VISIBLE
        loadingText.text = getString(R.string.loading_data_attempt, currentAttempt, maxAttempt)
    }

    private fun showLoaderDelete(name : String){
        loadingArea.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        loadingText.text = getString(R.string.loading_deleting_user, name)
    }

    private fun showError(error : String){
        context?.let {
            Toast.makeText(it, error, Toast.LENGTH_SHORT).show()
        }
    }


}